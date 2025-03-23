package com.att.tdp.popcorn_palace.showtime;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.movie.Movie;


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

    @GetMapping("/{showtimeId}")
    public ResponseEntity<?> find(@PathVariable Long showtimeId) {
        Optional<Showtime> showtimeOptional = showtimeRepository.findById(showtimeId);

        if (showtimeOptional.isPresent()) {
            Showtime showtime = showtimeOptional.get();
            return ResponseEntity.ok(showtime);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Showtime with id '" + showtimeId + "' not found.");
        }
    }
    
}
