package com.att.tdp.popcorn_palace.showtime;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // check if overlapping showtimes for the same theater
    @Query(
    "SELECT COUNT(s) > 0 FROM Showtime s WHERE " +
    "(:id IS NULL OR s.id != :id) AND " +
    "s.theater = :theater AND " +
    "((:startTime BETWEEN s.startTime AND s.endTime) OR " +
    "(:endTime BETWEEN s.startTime AND s.endTime) OR " +
    "(s.startTime BETWEEN :startTime AND :endTime))")
    boolean isOverlappingShowtime(Long id, String theater, java.time.LocalDateTime startTime, LocalDateTime endTime);

}
