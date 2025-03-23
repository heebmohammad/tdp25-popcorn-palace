package com.att.tdp.popcorn_palace.showtime;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // update a showtime
    @Modifying
    @Transactional
    @Query("UPDATE Showtime s SET s.movieId = :movieId, s.price = :price, s.theater = :theater, s.startTime = :startTime, s.endTime = :endTime WHERE s.id = :id")
    void updateShowtime(Long id, Long movieId, Float price, String theater, Date startTime, Date endTime);

    // check if overlapping showtimes for the same theater
    @Query("SELECT COUNT(s) > 0 FROM Showtime s WHERE s.theater = :theater AND ((:startTime BETWEEN s.startTime AND s.endTime) OR (:endTime BETWEEN s.startTime AND s.endTime) OR (s.startTime BETWEEN :startTime AND :endTime))")
    boolean isOverlappingShowtime(String theater, Date startTime, Date endTime);

}
