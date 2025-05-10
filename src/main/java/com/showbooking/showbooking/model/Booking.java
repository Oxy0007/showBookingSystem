package com.showbooking.showbooking.model;

import com.showbooking.showbooking.model.enums.BookingStatus;

import java.util.UUID;

public class Booking {
    private String id;
    private String userId;
    private String showId;
    private String slotId;
    private int size;
    private BookingStatus status;

    public Booking() {
        this.id = UUID.randomUUID().toString();
        this.status = BookingStatus.CONFIRMED;
    }

    public Booking(String userId, String showId, String slotId, int size) {
        this();
        this.userId = userId;
        this.showId = showId;
        this.slotId = slotId;
        this.size = size;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
