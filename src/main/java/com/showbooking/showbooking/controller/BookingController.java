package com.showbooking.showbooking.controller;

import com.showbooking.showbooking.dto.BookingRequest;
import com.showbooking.showbooking.dto.BookingResponse;
import com.showbooking.showbooking.model.Booking;
import com.showbooking.showbooking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookTicket(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.bookTicket(request);
        if (response.getBookingId() != null) {
            return ResponseEntity.ok("Booked. Booking id: " + response.getBookingId());
        } else {
            return ResponseEntity.ok(response.getMessage());
        }
    }

    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable String bookingId) {
        String result = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable String userName) {
        List<Booking> bookings = bookingService.getUserBookings(userName);
        return ResponseEntity.ok(bookings);
    }
}
