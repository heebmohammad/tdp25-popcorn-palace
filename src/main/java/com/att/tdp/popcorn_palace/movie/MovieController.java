package com.att.tdp.popcorn_palace.movie;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        try {
            Page<Movie> moviePage = movieRepository.findAll(pageable);
            return ResponseEntity.ok(moviePage.getContent());
        } catch (Exception e) {
            // 500 Internal Server Error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Something went wrong. Please try again later.");
        }
    }
    
}
