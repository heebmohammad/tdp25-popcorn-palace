package com.att.tdp.popcorn_palace.showtime;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Showtime {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Double price;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "theater")
    private String theater;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    // No-argument constructor for JPA
    public Showtime() {
        // Default constructor
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMovieId() {
        return movieId;
    }
    
    public void setMovieId(Long movieId) {
        if (movieId == null) {
            throw new IllegalArgumentException("movieId is required.");
        }
        this.movieId = movieId;
    }
    
    public String getTheater() {
        return theater;
    }
    
    public void setTheater(String theater) {
        if (theater == null || theater.trim().isEmpty()) {
            throw new IllegalArgumentException("Theater cannot be empty.");
        }
        this.theater = theater;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be be a positive number.");
        }
        this.price = price;
    }

    private void validateTimes() {
        if (this.startTime == null || this.endTime == null) {
            return;
        }

        if (this.startTime.compareTo(this.endTime) > 0) {
            throw new IllegalArgumentException("Start time cannot be after end time!");
        }
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("start time cannot be empty.");
        }
        this.startTime = startTime;
        this.validateTimes();
    }
    
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("end time cannot be empty.");
        }
        this.endTime = endTime;
        this.validateTimes();
    }
    
}
