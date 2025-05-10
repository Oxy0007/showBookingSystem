package com.showbooking.showbooking.dto;

import java.time.LocalTime;

public class BookingRequest {
    private String userName;
    private String showName;
    private LocalTime startTime;
    private int size;

    public BookingRequest() {
    }

    public BookingRequest(String userName, String showName, LocalTime startTime, int size) {
        this.userName = userName;
        this.showName = showName;
        this.startTime = startTime;
        this.size = size;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
