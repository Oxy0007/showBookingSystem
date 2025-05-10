package com.showbooking.showbooking.dto;

import com.showbooking.showbooking.model.enums.Genre;

public class ShowRegistrationRequest {
    private String name;
    private Genre genre;

    public ShowRegistrationRequest() {
    }

    public ShowRegistrationRequest(String name, Genre genre) {
        this.name = name;
        this.genre = genre;
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
}
