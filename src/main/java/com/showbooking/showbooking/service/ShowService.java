package com.showbooking.showbooking.service;

import com.showbooking.showbooking.dto.ShowAvailabilityResponse;
import com.showbooking.showbooking.dto.ShowRegistrationRequest;
import com.showbooking.showbooking.dto.SlotDetail;
import com.showbooking.showbooking.model.Show;
import com.showbooking.showbooking.model.ShowSlot;
import com.showbooking.showbooking.model.enums.Genre;

import java.util.List;

public interface ShowService {
    Show registerShow(ShowRegistrationRequest request);

    List<ShowSlot> onboardShowSlots(String showName, List<SlotDetail> slots);

    Show getShowByName(String name);

    ShowSlot findSlotByShowNameAndStartTime(String showName, String startTimeStr);

    List<ShowAvailabilityResponse> getAvailableShowsByGenre(Genre genre);

    String getTrendingShow();
}
