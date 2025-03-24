package com.att.tdp.popcorn_palace.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "release_year")
    private Integer releaseYear;

    // No-argument constructor for JPA
    public Movie() {
        // Default constructor
    }

    // Constructor with parameters
    public Movie(String title, String genre, Integer duration, Double rating, Integer releaseYear) {
        setTitle(title);
        setGenre(genre);
        setDuration(duration);
        setRating(rating);
        setReleaseYear(releaseYear);
    }

    public void validateFields() {
        if (title == null) {
            throw new IllegalStateException("'title' is missing.");
        }
        if (genre == null) {
            throw new IllegalStateException("'genre' is missing.");
        }
        if (duration == null) {
            throw new IllegalStateException("'duration' is missing.");
        }
        if (rating == null) {
            throw new IllegalStateException("'rating' is missing.");
        }
        if (releaseYear == null) {
            throw new IllegalStateException("'releaseYear' is missing.");
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
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty.");
        }
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Movie genre cannot be empty.");
        }
        this.genre = Genre.fromString(genre);
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        if (rating == null || rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Movie rating must be between 0 and 10.");
        }
        this.rating = rating;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        // First movie was made in 1888
        if (releaseYear == null || releaseYear < 1888 || releaseYear > 9999) {
            throw new IllegalArgumentException("Movie release year is invalid.");
        }
        this.releaseYear = releaseYear;
    }
}
