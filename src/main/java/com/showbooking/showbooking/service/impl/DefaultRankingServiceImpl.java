package com.showbooking.showbooking.service.impl;

import com.showbooking.showbooking.dto.ShowAvailabilityResponse;
import com.showbooking.showbooking.service.RankingService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultRankingServiceImpl implements RankingService {

    @Override
    public List<ShowAvailabilityResponse> rankShows(List<ShowAvailabilityResponse> availabilityResponses) {
        // Default strategy: rank by start time
        return availabilityResponses.stream()
                .sorted(Comparator.comparing(ShowAvailabilityResponse::getStartTime))
                .collect(Collectors.toList());
    }
}
