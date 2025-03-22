package com.att.tdp.popcorn_palace.movie;

import java.time.Year;

public record Movie(
    Integer id,
    String title,
    Genre genre,
    Integer duration,
    Float rating,
    Year releaseYear
) {

    public Movie {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty.");
        }
        if (genre == null) {
            throw new IllegalArgumentException("Movie genre cannot be empty.");
        }
        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException("Movie duration must be a positive number.");
        }
        if (rating == null ||rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Movie rating must be between 0 and 10.");
        }
        // First movie was made in 1888
        if (releaseYear == null || releaseYear.isBefore(Year.of(1888))) {
            throw new IllegalArgumentException("Movie release year is invalid.");
        }
    }
}
