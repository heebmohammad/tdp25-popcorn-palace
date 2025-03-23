package com.att.tdp.popcorn_palace.showtime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeRepository showtimeRepository;

    ShowtimeController(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    private void validateShowtimeOverlap(Showtime showtime) {
        if (showtimeRepository.isOverlappingShowtime(
            showtime.getTheater(), showtime.getStartTime(), showtime.getEndTime())) {
                throw new IllegalArgumentException("Overlapping showtime.");
        }
    }

    // Helper method to check if a showtime is not found by id
    private ResponseEntity<?> checkIfShowtimeNotFound(Long id) {
        if (!showtimeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Showtime with id '" + id + "' not found.");
        }
        return null; // No error found
    }
    
}
