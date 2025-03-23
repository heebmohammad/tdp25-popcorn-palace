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
}
