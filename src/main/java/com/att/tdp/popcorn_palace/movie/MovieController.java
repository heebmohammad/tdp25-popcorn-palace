package com.att.tdp.popcorn_palace.movie;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<Movie> findAll(Pageable pageable) {
        try {
            // Attempt to fetch the paginated results
            Page<Movie> moviePage = movieRepository.findAll(pageable);
            return moviePage.getContent();  // Return the content (list of movies)
        } catch (Exception e) {
            // Optionally, you could print it to the console if needed
            System.err.println("\n\n\n\nError fetching movies: " + e.getMessage());
            e.printStackTrace();
            return List.of();  // Return an empty list in case of an error
        }
    }
    
}
