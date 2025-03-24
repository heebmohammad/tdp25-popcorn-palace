package com.att.tdp.popcorn_palace.movie;

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

    // Helper method to check if a movie exists by title
    private ResponseEntity<?> checkIfMovieExists(String title) {
        if (movieRepository.existsByTitle(title)) {
            return ResponseEntity.badRequest()
                .body("A movie with the title '" + title + "' already exists.");
        }
        return null; // No error found
    }

    // Helper method to check if a movie is not found by title
    private ResponseEntity<?> checkIfMovieNotFound(String title) {
        if (!movieRepository.existsByTitle(title)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Movie with title '" + title + "' not found.");
        }
        return null; // No error found
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        return ResponseEntity.ok(moviePage.getContent());
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Movie movie) {
        movie.validateFields();
        // Check if a movie with the same title already exists
        ResponseEntity<?> existsResponse = checkIfMovieExists(movie.getTitle());
        if (existsResponse != null) {
            return existsResponse;
        }

        Movie savedMovie = movieRepository.save(movie);

        // Return a 200 Ok response with the saved movie
        return ResponseEntity.ok(savedMovie);
    }

    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<?> update(@RequestBody Movie newMovie, @PathVariable String movieTitle) {
        // Check if the movie with the given title exists
        ResponseEntity<?> notFoundResponse = checkIfMovieNotFound(movieTitle);
        if (notFoundResponse != null) {
            return notFoundResponse;
        }
        Long id = movieRepository.findByTitle(movieTitle).get().getId();
        newMovie.setId(id);

        newMovie.validateFields();
        if (!newMovie.getTitle().equals(movieTitle)) {
            // Check if a movie with the same new title already exists
            ResponseEntity<?> existsResponse = checkIfMovieExists(newMovie.getTitle());
            if (existsResponse != null) {
                return existsResponse;
            }
        }

        // update the movie
        movieRepository.save(newMovie);

        // Return a 200 Ok response
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<?> delete(@PathVariable String movieTitle) {
        // Check if the movie with the given title exists
        ResponseEntity<?> notFoundResponse = checkIfMovieNotFound(movieTitle);
        if (notFoundResponse != null) {
            return notFoundResponse;
        }

        movieRepository.deleteByTitle(movieTitle);
        // Return a 200 Ok response
        return ResponseEntity.ok(null);
    }
    
}
