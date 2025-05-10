package com.showbooking.showbooking.dto;

import java.time.LocalTime;

public class SlotDetail {
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;

    public SlotDetail() {
    }

    public SlotDetail(LocalTime startTime, LocalTime endTime, int capacity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
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
}
