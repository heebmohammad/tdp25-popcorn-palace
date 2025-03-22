package com.att.tdp.popcorn_palace.movie;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Genre {
    ACTION("Action"),
    DRAMA("Drama"),
    COMEDY("Comedy"),
    THRILLER("Thriller"),
    HORROR("Horror"),
    SCI_FI("Science Fiction"),
    ROMANCE("Romance"),
    DOCUMENTARY("Documentary"),
    ANIMATION("Animation"),
    FANTASY("Fantasy");

    private final String customName;

    Genre(String customName) {
        this.customName = customName;
    }

    @JsonValue
    public String getCustomName() {
        return customName;
    }

    public static Genre fromString(String genre) {
        switch (genre.toUpperCase()) {
            case "ACTION": return ACTION;
            case "DRAMA": return DRAMA;
            case "COMEDY": return COMEDY;
            case "THRILLER": return THRILLER;
            case "HORROR": return HORROR;
            case "SCI-FI": return SCI_FI;
            case "SCIENCE FICTION": return SCI_FI;
            case "ROMANCE": return ROMANCE;
            case "DOCUMENTARY": return DOCUMENTARY;
            case "ANIMATION": return ANIMATION;
            case "FANTASY": return FANTASY;
            default: throw new IllegalArgumentException("Unknown genre: " + genre);
        }
    }
}
