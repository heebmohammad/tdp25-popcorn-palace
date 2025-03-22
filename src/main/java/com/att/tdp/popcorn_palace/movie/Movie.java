package com.att.tdp.popcorn_palace.movie;

import java.time.Year;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Integer duration;
    private Float rating;
    private Year release_year;

    // No-argument constructor for JPA
    public Movie() {
        // Default constructor
    }

    // Constructor for manual creation
    public Movie(String title, Genre genre, Integer duration, Float rating, Year release_year) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.release_year = release_year;
        
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
        if (release_year == null || release_year.isBefore(Year.of(1888))) {
            throw new IllegalArgumentException("Movie release year is invalid.");
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty.");
        }
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Movie genre cannot be empty.");
        }
        this.genre = genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException("Movie duration must be a positive number.");
        }
        this.duration = duration;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        if (rating == null || rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Movie rating must be between 0 and 10.");
        }
        this.rating = rating;
    }

    public Year getrelease_year() {
        return release_year;
    }

    public void setrelease_year(Year release_year) {
        // First movie was made in 1888
        if (release_year == null || release_year.isBefore(Year.of(1888))) {
            throw new IllegalArgumentException("Movie release year is invalid.");
        }
        this.release_year = release_year;
    }
}
