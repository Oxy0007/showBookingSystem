package com.showbooking.showbooking.repository;

import com.showbooking.showbooking.model.Booking;
import com.showbooking.showbooking.model.WaitlistEntry;
import com.showbooking.showbooking.model.enums.BookingStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookingRepository {
    private final Map<String, Booking> bookings = new HashMap<>();
    private final Map<String, List<WaitlistEntry>> waitlistsBySlot = new HashMap<>();
    private final Map<String, Integer> showBookingCounts = new HashMap<>();

    public Booking save(Booking booking) {
        bookings.put(booking.getId(), booking);

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            showBookingCounts.put(booking.getShowId(),
                    showBookingCounts.getOrDefault(booking.getShowId(), 0) + booking.getSize());
        }

        return booking;
    }

    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }

    public List<Booking> findByUserId(String userId) {
        return bookings.values().stream()
                .filter(booking -> booking.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Booking> findByUserIdAndStatus(String userId, BookingStatus status) {
        return bookings.values().stream()
                .filter(booking -> booking.getUserId().equals(userId) && booking.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void updateBookingStatus(String bookingId, BookingStatus status) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            if (booking.getStatus() == BookingStatus.CONFIRMED && status != BookingStatus.CONFIRMED) {
                int currentCount = showBookingCounts.getOrDefault(booking.getShowId(), 0);
                showBookingCounts.put(booking.getShowId(), Math.max(0, currentCount - booking.getSize()));
            }

            booking.setStatus(status);
            bookings.put(bookingId, booking);
        }
    }

    public void addToWaitlist(WaitlistEntry entry) {
        List<WaitlistEntry> slotWaitlist = waitlistsBySlot.getOrDefault(entry.getSlotId(), new ArrayList<>());
        slotWaitlist.add(entry);
        waitlistsBySlot.put(entry.getSlotId(), slotWaitlist);
    }

    public Optional<WaitlistEntry> getNextWaitlistedEntry(String slotId, int requiredCapacity) {
        List<WaitlistEntry> slotWaitlist = waitlistsBySlot.getOrDefault(slotId, new ArrayList<>());

        return slotWaitlist.stream()
                .filter(entry -> entry.getSize() <= requiredCapacity)
                .min(Comparator.comparing(WaitlistEntry::getCreatedAt));
    }

    public void removeFromWaitlist(WaitlistEntry entry) {
        List<WaitlistEntry> slotWaitlist = waitlistsBySlot.getOrDefault(entry.getSlotId(), new ArrayList<>());
        slotWaitlist.remove(entry);
        waitlistsBySlot.put(entry.getSlotId(), slotWaitlist);
    }

    public Map<String, Integer> getTrendingShows() {
        return Collections.unmodifiableMap(showBookingCounts);
    }

    public String getMostTrendingShow() {
        return showBookingCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
