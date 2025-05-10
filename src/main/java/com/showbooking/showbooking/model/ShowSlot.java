package com.showbooking.showbooking.model;

import java.time.LocalTime;
import java.util.UUID;

public class ShowSlot {
    private String id;
    private String showId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;
    private int availableCapacity;

    public ShowSlot() {
        this.id = UUID.randomUUID().toString();
    }

    public ShowSlot(String showId, LocalTime startTime, LocalTime endTime, int capacity) {
        this();
        this.showId = showId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.availableCapacity = capacity;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public boolean isAvailable(int requiredCapacity) {
        return this.availableCapacity >= requiredCapacity;
    }

    public void decreaseAvailableCapacity(int bookingSize) {
        this.availableCapacity -= bookingSize;
    }

    public void increaseAvailableCapacity(int bookingSize) {
        this.availableCapacity += bookingSize;
    }

    @Override
    public String toString() {
        return "(" + startTime.toString() + "-" + endTime.toString() + ") " + availableCapacity;
    }
}
