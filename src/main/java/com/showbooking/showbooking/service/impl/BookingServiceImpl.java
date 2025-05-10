package com.showbooking.showbooking.service.impl;

import com.showbooking.showbooking.dto.BookingRequest;
import com.showbooking.showbooking.dto.BookingResponse;
import com.showbooking.showbooking.exception.CommonCustomException;
import com.showbooking.showbooking.model.*;
import com.showbooking.showbooking.model.enums.BookingStatus;
import com.showbooking.showbooking.repository.BookingRepository;
import com.showbooking.showbooking.repository.ShowRepository;
import com.showbooking.showbooking.service.BookingService;
import com.showbooking.showbooking.service.ShowService;
import com.showbooking.showbooking.service.UserService;
import com.showbooking.showbooking.util.TimeSlotUtil;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final ShowService showService;
    private final UserService userService;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            ShowRepository showRepository,
            ShowService showService,
            UserService userService
    ) {
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.showService = showService;
        this.userService = userService;
    }

    @Override
    public BookingResponse bookTicket(BookingRequest request) {
        User user = userService.getOrCreateUser(request.getUserName());
        Show show = showService.getShowByName(request.getShowName());

        List<ShowSlot> slots = showRepository.findSlotsByShowId(show.getId());
        Optional<ShowSlot> slotOpt = slots.stream()
                .filter(slot -> slot.getStartTime().equals(request.getStartTime()))
                .findFirst();

        if (slotOpt.isEmpty()) {
            throw new CommonCustomException("No slot available for the show at the specified time");
        }

        ShowSlot slot = slotOpt.get();

        checkUserTimeConflict(user.getId(), slot.getStartTime(), slot.getEndTime());

        if (!slot.isAvailable(request.getSize())) {
            WaitlistEntry waitlistEntry = new WaitlistEntry(
                    user.getId(),
                    show.getId(),
                    slot.getId(),
                    request.getSize()
            );
            bookingRepository.addToWaitlist(waitlistEntry);
            return new BookingResponse("Added to waitlist. You'll be notified if a spot becomes available.", null);
        }

        Booking booking = new Booking(
                user.getId(),
                show.getId(),
                slot.getId(),
                request.getSize()
        );

        Booking savedBooking = bookingRepository.save(booking);

        slot.decreaseAvailableCapacity(request.getSize());
        showRepository.saveSlot(slot);

        user.addBooking(savedBooking.getId());
        userService.getOrCreateUser(user.getName());

        return new BookingResponse("Booked.", savedBooking.getId());
    }

    private void checkUserTimeConflict(String userId, LocalTime startTime, LocalTime endTime) {
        List<Booking> userBookings = bookingRepository.findByUserIdAndStatus(userId, BookingStatus.CONFIRMED);

        for (Booking booking : userBookings) {
            Optional<ShowSlot> bookedSlotOpt = showRepository.findSlotById(booking.getSlotId());
            if (bookedSlotOpt.isPresent()) {
                ShowSlot bookedSlot = bookedSlotOpt.get();
                if (TimeSlotUtil.doSlotsOverlap(
                        bookedSlot.getStartTime(), bookedSlot.getEndTime(),
                        startTime, endTime)) {
                    throw new CommonCustomException(
                            "User already has a booking during this time slot"
                    );
                }
            }
        }
    }

    @Override
    public String cancelBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return "Booking is already cancelled";
        }

        bookingRepository.updateBookingStatus(bookingId, BookingStatus.CANCELLED);

        Optional<ShowSlot> slotOpt = showRepository.findSlotById(booking.getSlotId());
        if (slotOpt.isPresent()) {
            ShowSlot slot = slotOpt.get();
            slot.increaseAvailableCapacity(booking.getSize());
            showRepository.saveSlot(slot);

            processWaitlist(slot);
        }

        User user = userService.getUserById(booking.getUserId());
        user.removeBooking(bookingId);
        userService.getOrCreateUser(user.getName());

        return "Booking Canceled";
    }

    private void processWaitlist(ShowSlot slot) {
        Optional<WaitlistEntry> nextInWaitlist = bookingRepository.getNextWaitlistedEntry(
                slot.getId(), slot.getAvailableCapacity()
        );

        if (nextInWaitlist.isPresent()) {
            WaitlistEntry entry = nextInWaitlist.get();

            Booking newBooking = new Booking(
                    entry.getUserId(),
                    entry.getShowId(),
                    entry.getSlotId(),
                    entry.getSize()
            );

            Booking savedBooking = bookingRepository.save(newBooking);

            slot.decreaseAvailableCapacity(entry.getSize());
            showRepository.saveSlot(slot);

            User user = userService.getUserById(entry.getUserId());
            user.addBooking(savedBooking.getId());
            userService.getOrCreateUser(user.getName());

            bookingRepository.removeFromWaitlist(entry);
        }
    }

    @Override
    public List<Booking> getUserBookings(String userName) {
        User user = userService.getUserByName(userName);
        if (user == null) {
            return List.of();
        }
        return bookingRepository.findByUserId(user.getId());
    }

    @Override
    public Booking getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CommonCustomException("Booking not found with id: " + bookingId));
    }
}
