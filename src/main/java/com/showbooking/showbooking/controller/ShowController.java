package com.showbooking.showbooking.controller;

import com.showbooking.showbooking.dto.ShowAvailabilityResponse;
import com.showbooking.showbooking.dto.ShowRegistrationRequest;
import com.showbooking.showbooking.dto.SlotOnboardingRequest;
import com.showbooking.showbooking.model.Show;
import com.showbooking.showbooking.model.ShowSlot;
import com.showbooking.showbooking.model.enums.Genre;
import com.showbooking.showbooking.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shows")
public class ShowController {
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerShow(@RequestBody ShowRegistrationRequest request) {
        Show show = showService.registerShow(request);
        return ResponseEntity.ok(show.getName() + " show is registered !!");
    }

    @PostMapping("/onboardSlots")
    public ResponseEntity<String> onboardShowSlots(@RequestBody SlotOnboardingRequest request) {
        List<ShowSlot> slots = showService.onboardShowSlots(request.getShowName(), request.getSlots());
        return ResponseEntity.ok("Done!");
    }

    @GetMapping("/availableByGenre/{genre}")
    public ResponseEntity<List<String>> showAvailByGenre(@PathVariable String genre) {
        Genre genreEnum = Genre.valueOf(genre.toUpperCase());
        List<ShowAvailabilityResponse> availableShows = showService.getAvailableShowsByGenre(genreEnum);

        List<String> formattedResponses = availableShows.stream()
                .map(ShowAvailabilityResponse::toString)
                .collect(Collectors.toList());

        return ResponseEntity.ok(formattedResponses);
    }

    @GetMapping("/trending")
    public ResponseEntity<String> getTrendingShow() {
        String trendingShow = showService.getTrendingShow();
        return ResponseEntity.ok("Trending Show: " + trendingShow);
    }
}