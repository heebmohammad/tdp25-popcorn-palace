package com.att.tdp.popcorn_palace.movie;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        return ResponseEntity.ok(moviePage.getContent());
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Movie movie) {
        // Check if a movie with the same title already exists
        Optional<Movie> existingMovie = movieRepository.findByTitle(movie.getTitle());
        if (existingMovie.isPresent()) {
            // Return a 400 Bad Request status with an error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A movie with the title '" + movie.getTitle() + "' already exists.");
        }

        Movie savedMovie = movieRepository.save(movie);

        // Return a 200 Ok response with the saved movie
        return ResponseEntity.ok(savedMovie);
    }
    
}
