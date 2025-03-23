package com.att.tdp.popcorn_palace.booking;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    // Check if a booking already exists for a given showtime and seat
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.showtimeId = :showtimeId AND b.seatNumber = :seatNumber")
    boolean isSeatAlreadyBooked(Long showtimeId, Integer seatNumber);
}
