package com.showbooking.showbooking.util;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSlotUtil {
    public static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
    public static final LocalTime CLOSING_TIME = LocalTime.of(21, 0);
    public static final Duration SLOT_DURATION = Duration.ofHours(1);

    public static boolean isValidSlotDuration(LocalTime startTime, LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.equals(SLOT_DURATION);
    }

    public static boolean isWithinOperatingHours(LocalTime time) {
        return !time.isBefore(OPENING_TIME) && !time.isAfter(CLOSING_TIME);
    }

    public static boolean isValidSlotTime(LocalTime startTime, LocalTime endTime) {
        return isWithinOperatingHours(startTime) &&
                isWithinOperatingHours(endTime) &&
                isValidSlotDuration(startTime, endTime);
    }

    public static boolean doSlotsOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public static String formatTimeRange(LocalTime startTime, LocalTime endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(formatter) + "-" + endTime.format(formatter);
    }

    public static LocalTime parseTime(String timeString) {
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
