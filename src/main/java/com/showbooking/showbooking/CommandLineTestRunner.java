package com.showbooking.showbooking;

import com.showbooking.showbooking.dto.BookingRequest;
import com.showbooking.showbooking.dto.ShowRegistrationRequest;
import com.showbooking.showbooking.dto.SlotDetail;
import com.showbooking.showbooking.dto.SlotOnboardingRequest;
import com.showbooking.showbooking.model.enums.Genre;
import com.showbooking.showbooking.service.BookingService;
import com.showbooking.showbooking.service.ShowService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class CommandLineTestRunner implements CommandLineRunner {

    private final ShowService showService;
    private final BookingService bookingService;
    private final Scanner scanner = new Scanner(System.in);

    public CommandLineTestRunner(ShowService showService, BookingService bookingService) {
        this.showService = showService;
        this.bookingService = bookingService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Welcome to Show Booking System!");
        System.out.println("Available commands:");
        System.out.println("1. registerShow: <show_name> -> <genre>");
        System.out.println("2. onboardShowSlots: <show_name> <start_time>-<end_time> <capacity>, ...");
        System.out.println("3. showAvailByGenre: <genre>");
        System.out.println("4. bookTicket: (<user_name>, <show_name>, <start_time>, <size>)");
        System.out.println("5. cancelBookingId: <booking_id>");
        System.out.println("6. getTrendingShow");
        System.out.println("7. exit");

        boolean running = true;
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equals("exit")) {
                running = false;
                continue;
            }

            try {
                if (input.startsWith("registerShow:")) {
                    handleRegisterShow(input);
                } else if (input.startsWith("onboardShowSlots:")) {
                    handleOnboardShowSlots(input);
                } else if (input.startsWith("showAvailByGenre:")) {
                    handleShowAvailByGenre(input);
                } else if (input.startsWith("bookTicket:")) {
                    handleBookTicket(input);
                } else if (input.startsWith("cancelBookingId:")) {
                    handleCancelBooking(input);
                } else if (input.startsWith("getTrendingShow")) {
                    handleGetTrendingShow();
                } else {
                    System.out.println("Unknown command. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleRegisterShow(String input) {
        // Extract show name and genre from input: registerShow: TMKOC -> Comedy
        String[] parts = input.substring("registerShow:".length()).trim().split("->");
        if (parts.length != 2) {
            System.out.println("Invalid format. Use: registerShow: <show_name> -> <genre>");
            return;
        }

        String showName = parts[0].trim();
        String genreStr = parts[1].trim();

        Genre genre;
        try {
            genre = Genre.valueOf(genreStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid genre. Available genres: COMEDY, THEATRE, TECH, SINGING");
            return;
        }

        ShowRegistrationRequest request = new ShowRegistrationRequest(showName, genre);
        showService.registerShow(request);
        System.out.println(showName + " show is registered !!");
    }

    private void handleOnboardShowSlots(String input) {
        // Extract show name and slots from input: onboardShowSlots: TMKOC 9:00-10:00 3, 12:00-13:00 2, 15:00-16:00 5
        String content = input.substring("onboardShowSlots:".length()).trim();

        String[] parts = content.split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Invalid format. Use: onboardShowSlots: <show_name> <start_time>-<end_time> <capacity>, ...");
            return;
        }

        String showName = parts[0];
        String slotsStr = parts[1];

        // Parse slots: 9:00-10:00 3, 12:00-13:00 2, 15:00-16:00 5
        String[] slotParts = slotsStr.split(",");
        List<SlotDetail> slots = new ArrayList<>();

        for (String slotPart : slotParts) {
            String[] slotInfo = slotPart.trim().split("\\s+");
            if (slotInfo.length != 2) {
                System.out.println("Invalid slot format: " + slotPart);
                return;
            }

            String timeRange = slotInfo[0]; // 9:00-10:00
            int capacity;
            try {
                capacity = Integer.parseInt(slotInfo[1]); // 3
            } catch (NumberFormatException e) {
                System.out.println("Invalid capacity: " + slotInfo[1]);
                return;
            }

            String[] times = timeRange.split("-");
            if (times.length != 2) {
                System.out.println("Invalid time range: " + timeRange);
                return;
            }

            try {
                LocalTime startTime = LocalTime.parse(times[0]);
                LocalTime endTime = LocalTime.parse(times[1]);

                SlotDetail slot = new SlotDetail(startTime, endTime, capacity);
                slots.add(slot);
            } catch (Exception e) {
                System.out.println("Invalid time format. Use HH:mm format, e.g., 09:00-10:00");
                return;
            }
        }

        SlotOnboardingRequest request = new SlotOnboardingRequest(showName, slots);
        showService.onboardShowSlots(showName, slots);
        System.out.println("Done!");
    }

    private void handleShowAvailByGenre(String input) {
        // Extract genre from input: showAvailByGenre: Comedy
        String genreStr = input.substring("showAvailByGenre:".length()).trim();

        Genre genre;
        try {
            genre = Genre.valueOf(genreStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid genre. Available genres: COMEDY, THEATRE, TECH, SINGING");
            return;
        }

        var availableShows = showService.getAvailableShowsByGenre(genre);
        if (availableShows.isEmpty()) {
            System.out.println("No shows available for genre: " + genre);
            return;
        }

        System.out.println("Available shows for genre " + genre + ":");
        for (var show : availableShows) {
            System.out.println(show.toString());
        }
    }

    private void handleBookTicket(String input) {
        // Parse input: bookTicket: (user1, TMKOC, 09:00, 2)
        String content = input.substring("bookTicket:".length()).trim();

        // Remove parentheses and split by comma
        if (!content.startsWith("(") || !content.endsWith(")")) {
            System.out.println("Invalid format. Use: bookTicket: (<user_name>, <show_name>, <start_time>, <size>)");
            return;
        }

        content = content.substring(1, content.length() - 1);
        String[] parts = content.split(",");

        if (parts.length != 4) {
            System.out.println("Invalid format. Use: bookTicket: (<user_name>, <show_name>, <start_time>, <size>)");
            return;
        }

        String userName = parts[0].trim();
        String showName = parts[1].trim();
        String startTimeStr = parts[2].trim();
        int size;

        try {
            size = Integer.parseInt(parts[3].trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ticket size: " + parts[3].trim());
            return;
        }

        try {
            LocalTime startTime = LocalTime.parse(startTimeStr);

            BookingRequest request = new BookingRequest(userName, showName, startTime, size);
            var response = bookingService.bookTicket(request);

            if (response.getBookingId() != null) {
                System.out.println("Booked. Booking id: " + response.getBookingId());
            } else {
                System.out.println(response.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Invalid time format. Use HH:mm format, e.g., 09:00");
            return;
        }
    }

    private void handleCancelBooking(String input) {
        // Parse input: cancelBookingId: booking123
        String bookingId = input.substring("cancelBookingId:".length()).trim();

        if (bookingId.isEmpty()) {
            System.out.println("Invalid format. Use: cancelBookingId: <booking_id>");
            return;
        }

        String result = bookingService.cancelBooking(bookingId);
        System.out.println(result);
    }

    private void handleGetTrendingShow() {
        String trendingShow = showService.getTrendingShow();
        System.out.println("Trending Show: " + trendingShow);
    }
}
