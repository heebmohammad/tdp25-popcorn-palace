package com.att.tdp.popcorn_palace.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Movie savedMovie = movieRepository.save(movie);

        // Return a 200 Ok response with the saved movie
        return ResponseEntity.ok(savedMovie);
    }
    
}
