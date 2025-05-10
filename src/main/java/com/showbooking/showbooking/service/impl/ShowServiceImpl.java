package com.showbooking.showbooking.service.impl;

import com.showbooking.showbooking.dto.ShowAvailabilityResponse;
import com.showbooking.showbooking.dto.ShowRegistrationRequest;
import com.showbooking.showbooking.dto.SlotDetail;
import com.showbooking.showbooking.exception.CommonCustomException;
import com.showbooking.showbooking.model.Show;
import com.showbooking.showbooking.model.ShowSlot;
import com.showbooking.showbooking.model.enums.Genre;
import com.showbooking.showbooking.repository.BookingRepository;
import com.showbooking.showbooking.repository.ShowRepository;
import com.showbooking.showbooking.service.RankingService;
import com.showbooking.showbooking.service.ShowService;
import com.showbooking.showbooking.util.TimeSlotUtil;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final RankingService rankingService;

    public ShowServiceImpl(ShowRepository showRepository, BookingRepository bookingRepository, RankingService rankingService) {
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.rankingService = rankingService;
    }

    @Override
    public Show registerShow(ShowRegistrationRequest request) {
        Show show = new Show(request.getName(), request.getGenre());
        return showRepository.save(show);
    }

    @Override
    public List<ShowSlot> onboardShowSlots(String showName, List<SlotDetail> slots) {
        Show show = getShowByName(showName);
        List<ShowSlot> existingSlots = showRepository.findSlotsByShowId(show.getId());
        List<ShowSlot> newSlots = new ArrayList<>();

        for (SlotDetail slotDetail : slots) {

            if (!TimeSlotUtil.isValidSlotDuration(slotDetail.getStartTime(), slotDetail.getEndTime())) {
                throw new CommonCustomException("Show timings are of 1 hour only");
            }

            if (!TimeSlotUtil.isWithinOperatingHours(slotDetail.getStartTime()) ||
                    !TimeSlotUtil.isWithinOperatingHours(slotDetail.getEndTime())) {
                throw new CommonCustomException("Show timings must be between 9:00 and 21:00");
            }

            boolean hasOverlap = existingSlots.stream()
                    .anyMatch(existing -> TimeSlotUtil.doSlotsOverlap(
                            existing.getStartTime(), existing.getEndTime(),
                            slotDetail.getStartTime(), slotDetail.getEndTime()
                    ));

            if (hasOverlap) {
                throw new CommonCustomException("Overlapping slots cannot be provided for a particular show");
            }

            ShowSlot slot = new ShowSlot(
                    show.getId(),
                    slotDetail.getStartTime(),
                    slotDetail.getEndTime(),
                    slotDetail.getCapacity()
            );

            ShowSlot savedSlot = showRepository.saveSlot(slot);
            show.addSlot(savedSlot);
            newSlots.add(savedSlot);
        }

        return newSlots;
    }

    @Override
    public Show getShowByName(String name) {
        return showRepository.findByName(name)
                .orElseThrow(() -> new CommonCustomException("Show not found with name: " + name));
    }

    @Override
    public ShowSlot findSlotByShowNameAndStartTime(String showName, String startTimeStr) {
        Show show = getShowByName(showName);
        LocalTime startTime = TimeSlotUtil.parseTime(startTimeStr);

        List<ShowSlot> slots = showRepository.findSlotsByShowId(show.getId());
        Optional<ShowSlot> slot = slots.stream()
                .filter(s -> s.getStartTime().equals(startTime))
                .findFirst();

        return slot.orElseThrow(() -> new CommonCustomException("Slot not found for show: " + showName + " at time: " + startTimeStr));
    }

    @Override
    public List<ShowAvailabilityResponse> getAvailableShowsByGenre(Genre genre) {
        List<Show> shows = showRepository.findByGenre(genre);
        List<ShowAvailabilityResponse> availabilityResponses = new ArrayList<>();

        for (Show show : shows) {
            List<ShowSlot> slots = showRepository.findSlotsByShowId(show.getId());

            for (ShowSlot slot : slots) {
                if (slot.getAvailableCapacity() > 0) {
                    ShowAvailabilityResponse response = new ShowAvailabilityResponse(
                            show.getName(),
                            slot.getStartTime(),
                            slot.getEndTime(),
                            slot.getAvailableCapacity()
                    );
                    availabilityResponses.add(response);
                }
            }
        }

        return rankingService.rankShows(availabilityResponses);
    }

    @Override
    public String getTrendingShow() {
        String trendingShowId = bookingRepository.getMostTrendingShow();
        if (trendingShowId == null) {
            return "No trending show found";
        }

        Optional<Show> show = showRepository.findById(trendingShowId);
        return show.map(Show::getName).orElse("Unknown show");
    }
}
