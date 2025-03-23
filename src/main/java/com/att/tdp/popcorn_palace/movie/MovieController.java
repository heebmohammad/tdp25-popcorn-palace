package com.att.tdp.popcorn_palace.movie;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<?> update(@RequestBody Movie newMovie, @PathVariable String movieTitle) {
        // Check if the movie with the given title exists
        Optional<Movie> oldMovie = movieRepository.findByTitle(movieTitle);
        if (oldMovie.isEmpty()) {
            // Return a 404 Not Found if the movie doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Movie with title '" + movieTitle + "' not found.");
        }

        // Check if a movie with the same title already exists
        Optional<Movie> existingMovie = movieRepository.findByTitle(newMovie.getTitle());
        if (existingMovie.isPresent()) {
            // Return a 400 Bad Request status with an error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A movie with the title '" + newMovie.getTitle() + "' already exists.");
        }

         // update the movie
        movieRepository.updateMovieByTitle(
            movieTitle,
            newMovie.getTitle(),
            newMovie.getGenre(),
            newMovie.getDuration(),
            newMovie.getRating(),
            newMovie.getReleaseYear()
        );

        // Return a 200 Ok response
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<?> delete(@PathVariable String movieTitle) {
        // Check if the movie with the given title exists
        Optional<Movie> oldMovie = movieRepository.findByTitle(movieTitle);
        if (oldMovie.isEmpty()) {
            // Return a 404 Not Found if the movie doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Movie with title '" + movieTitle + "' not found.");
        }

        movieRepository.deleteByTitle(movieTitle);
        // Return a 200 Ok response
        return ResponseEntity.ok(null);
    }
    
}
