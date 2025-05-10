package com.showbooking.showbooking.dto;

public class BookingResponse {
    private String message;
    private String bookingId;

    public BookingResponse() {
    }

    public BookingResponse(String message, String bookingId) {
        this.message = message;
        this.bookingId = bookingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
