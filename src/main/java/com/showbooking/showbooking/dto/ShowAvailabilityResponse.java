package com.showbooking.showbooking.dto;

import java.time.LocalTime;

public class ShowAvailabilityResponse {
    private String showName;
    private LocalTime startTime;
    private LocalTime endTime;
    private int availableCapacity;

    public ShowAvailabilityResponse() {
    }

    public ShowAvailabilityResponse(String showName, LocalTime startTime, LocalTime endTime, int availableCapacity) {
        this.showName = showName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.availableCapacity = availableCapacity;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
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

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    @Override
    public String toString() {
        return showName + ": (" + startTime + "-" + endTime + ") " + availableCapacity;
    }
}
