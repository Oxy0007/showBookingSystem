package com.showbooking.showbooking.model;

import com.showbooking.showbooking.model.enums.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Show {
    private String id;
    private String name;
    private Genre genre;
    private List<ShowSlot> slots;

    public Show() {
        this.id = UUID.randomUUID().toString();
        this.slots = new ArrayList<>();
    }

    public Show(String name, Genre genre) {
        this();
        this.name = name;
        this.genre = genre;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<ShowSlot> getSlots() {
        return slots;
    }

    public void addSlot(ShowSlot slot) {
        this.slots.add(slot);
    }
}
