package com.showbooking.showbooking.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class WaitlistEntry {
    private String id;
    private String userId;
    private String showId;
    private String slotId;
    private int size;
    private LocalDateTime createdAt;

    public WaitlistEntry() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public WaitlistEntry(String userId, String showId, String slotId, int size) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
