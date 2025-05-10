package com.showbooking.showbooking.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String id;
    private String name;
    private List<String> bookingIds;

    public User() {
        this.id = UUID.randomUUID().toString();
        this.bookingIds = new ArrayList<>();
    }

    public User(String name) {
        this();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBookingIds() {
        return bookingIds;
    }

    public void addBooking(String bookingId) {
        this.bookingIds.add(bookingId);
    }

    public void removeBooking(String bookingId) {
        this.bookingIds.remove(bookingId);
    }
}
