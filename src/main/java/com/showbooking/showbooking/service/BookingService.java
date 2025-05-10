package com.showbooking.showbooking.service;

import com.showbooking.showbooking.dto.BookingRequest;
import com.showbooking.showbooking.dto.BookingResponse;
import com.showbooking.showbooking.model.Booking;

import java.util.List;

public interface BookingService {
    /**
     * Book a ticket for a show
     * @param request Booking request with user, show, time and size
     * @return Booking response with ID
     */
    BookingResponse bookTicket(BookingRequest request);

    /**
     * Cancel a booking by its ID
     * @param bookingId ID of the booking to cancel
     * @return Status message
     */
    String cancelBooking(String bookingId);

    /**
     * Get all bookings for a user
     * @param userName Name of the user
     * @return List of bookings
     */
    List<Booking> getUserBookings(String userName);

    /**
     * Get booking by ID
     * @param bookingId ID of the booking
     * @return The booking if found
     */
    Booking getBookingById(String bookingId);
}
