package com.showbooking.showbooking.repository;

import com.showbooking.showbooking.model.Show;
import com.showbooking.showbooking.model.ShowSlot;
import com.showbooking.showbooking.model.enums.Genre;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ShowRepository {
    private final Map<String, Show> shows = new HashMap<>();
    private final Map<String, ShowSlot> slots = new HashMap<>();

    public Show save(Show show) {
        shows.put(show.getId(), show);
        return show;
    }

    public Optional<Show> findById(String id) {
        return Optional.ofNullable(shows.get(id));
    }

    public Optional<Show> findByName(String name) {
        return shows.values().stream()
                .filter(show -> show.getName().equals(name))
                .findFirst();
    }

    public List<Show> findByGenre(Genre genre) {
        return shows.values().stream()
                .filter(show -> show.getGenre() == genre)
                .collect(Collectors.toList());
    }

    public List<Show> findAll() {
        return new ArrayList<>(shows.values());
    }

    public ShowSlot saveSlot(ShowSlot slot) {
        slots.put(slot.getId(), slot);
        return slot;
    }

    public Optional<ShowSlot> findSlotById(String id) {
        return Optional.ofNullable(slots.get(id));
    }

    public List<ShowSlot> findSlotsByShowId(String showId) {
        return slots.values().stream()
                .filter(slot -> slot.getShowId().equals(showId))
                .collect(Collectors.toList());
    }
}
