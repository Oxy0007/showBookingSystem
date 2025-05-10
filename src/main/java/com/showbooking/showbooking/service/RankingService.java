package com.showbooking.showbooking.service;

import com.showbooking.showbooking.dto.ShowAvailabilityResponse;

import java.util.List;

public interface RankingService {
    /**
     * Rank show availability responses by a strategy
     * @param availabilityResponses List of show availability responses
     * @return Ranked list of show availability responses
     */
    List<ShowAvailabilityResponse> rankShows(List<ShowAvailabilityResponse> availabilityResponses);
}
