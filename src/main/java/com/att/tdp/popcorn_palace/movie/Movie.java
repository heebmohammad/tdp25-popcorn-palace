package com.att.tdp.popcorn_palace.movie;

import java.time.Year;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public record Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID in the database
    Long id,
    String title,
    Genre genre,
    Integer duration,
    Float rating,
    Year releaseYear
) {

    // No-argument constructor for JPA
    public Movie() {
        this(null, null, null, null, null, null);
    }

    // Constructor for manual creation
    public Movie(String title, Genre genre, Integer duration, Float rating, Year releaseYear) {
        this(null, title, genre, duration, rating, releaseYear); // ID will be auto-generated
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty.");
        }
        if (genre == null) {
            throw new IllegalArgumentException("Movie genre cannot be empty.");
        }
        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException("Movie duration must be a positive number.");
        }
        if (rating == null || rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Movie rating must be between 0 and 10.");
        }
        // First movie was made in 1888
        if (releaseYear == null || releaseYear.isBefore(Year.of(1888))) {
            throw new IllegalArgumentException("Movie release year is invalid.");
        }
    }
}
