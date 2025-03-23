package com.att.tdp.popcorn_palace.booking;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Booking {
    public static final Integer MAX_SEATS = 9999;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id")
    private UUID bookingId;

    @JsonIgnore
    @Column(name = "showtime_id")
    private Long showtimeId;

    @JsonIgnore
    @Column(name = "seat_number")
    private Integer seatNumber;

    @JsonIgnore
    @Column(name = "user_id")
    private UUID userId;

    // No-argument constructor for JPA
    public Booking() {
        // Default constructor
    }

    // Getters and Setters
    
    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        if (showtimeId == null) {
            throw new IllegalArgumentException("showtimeId is required.");
        }
        this.showtimeId = showtimeId;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        if (seatNumber == null || seatNumber < 0) {
            throw new IllegalArgumentException("Seat number must be a positive number.");
        }
        if (seatNumber > MAX_SEATS) {
            throw new IllegalArgumentException("Exceeding max number of seats: " + MAX_SEATS);
        }
        this.seatNumber = seatNumber;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required.");
        }
        this.userId = userId;
    }
}
