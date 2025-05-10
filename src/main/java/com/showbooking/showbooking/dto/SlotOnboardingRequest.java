package com.showbooking.showbooking.dto;

import java.util.List;

public class SlotOnboardingRequest {
    private String showName;
    private List<SlotDetail> slots;

    public SlotOnboardingRequest() {
    }

    public SlotOnboardingRequest(String showName, List<SlotDetail> slots) {
        this.showName = showName;
        this.slots = slots;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public List<SlotDetail> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotDetail> slots) {
        this.slots = slots;
    }
}
