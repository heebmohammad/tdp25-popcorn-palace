package com.att.tdp.popcorn_palace.showtime;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.movie.MovieRepository;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    ShowtimeController(ShowtimeRepository showtimeRepository, MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    private ResponseEntity<?> checkIfShowtimeOverlap(Showtime showtime) {
        if (showtimeRepository.isOverlappingShowtime(
            showtime.getId(), showtime.getTheater(), showtime.getStartTime(), showtime.getEndTime())) {
                return ResponseEntity.badRequest().body("Overlapping showtime.");
        }
        return null; // No error found
    }

    // Helper method to check if a movie exist by id
    private ResponseEntity<?> checkIfMovieExist(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            return ResponseEntity.badRequest()
                .body("Movie with id '" + movieId + "' does not exist.");
        }
        return null; // No error found
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

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Showtime showtime) {
        showtime.validateFields();
        ResponseEntity<?> movieExistResponse = checkIfMovieExist(showtime.getMovieId());
        if (movieExistResponse != null) {
            return movieExistResponse;
        }

        ResponseEntity<?> overlapResponse = checkIfShowtimeOverlap(showtime);
        if (overlapResponse != null) {
            return overlapResponse;
        }

        Showtime savedShowtime = showtimeRepository.save(showtime);

        // Return a 200 Ok response with the saved showtime
        return ResponseEntity.ok(savedShowtime);
    }

    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<?> update(@RequestBody Showtime showtime, @PathVariable Long showtimeId) {
        showtime.validateFields();
        // Check if the showtime with the given showtimeId exists
        ResponseEntity<?> notFoundResponse = checkIfShowtimeNotFound(showtimeId);
        if (notFoundResponse != null) {
            return notFoundResponse;
        }
        showtime.setId(showtimeId);

        ResponseEntity<?> movieExistResponse = checkIfMovieExist(showtime.getMovieId());
        if (movieExistResponse != null) {
            return movieExistResponse;
        }

        ResponseEntity<?> overlapResponse = checkIfShowtimeOverlap(showtime);
        if (overlapResponse != null) {
            return overlapResponse;
        }

        // update the showtime
        showtimeRepository.save(showtime);

        // Return a 200 Ok response
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<?> delete(@PathVariable Long showtimeId) {
        // Check if the showtime with the given showtimeId exists
        ResponseEntity<?> notFoundResponse = checkIfShowtimeNotFound(showtimeId);
        if (notFoundResponse != null) {
            return notFoundResponse;
        }

        showtimeRepository.deleteById(showtimeId);;
        // Return a 200 Ok response
        return ResponseEntity.ok(null);
    }
    
}
