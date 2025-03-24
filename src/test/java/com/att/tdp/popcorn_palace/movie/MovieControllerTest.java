package com.att.tdp.popcorn_palace.movie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieRepository movieRepository;

    // Test for "GET /movies/all"
    @Test
    public void testFindAllMovies() throws Exception {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie1", "Action", 120, 7.5, 2021),
            new Movie("Movie2", "Comedy", 110, 6.5, 2020)
        );

        Page<Movie> moviePage = new PageImpl<>(movies);
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(moviePage);

        mockMvc.perform(get("/movies/all").param("page", "0").param("size", "10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("Movie1"))
               .andExpect(jsonPath("$[1].title").value("Movie2"));
    }

    // Test for "POST /movies"
    @Test
    public void testCreateMovie() throws Exception {
        Movie newMovie = new Movie("Movie3", "Drama", 130, 8.0, 2022);
        when(movieRepository.existsByTitle(newMovie.getTitle())).thenReturn(false);
        when(movieRepository.save(any(Movie.class))).thenReturn(newMovie);

        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Movie3\", \"genre\": \"Drama\", \"duration\": 130, \"rating\": 8.0, \"releaseYear\": 2022}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Movie3"))
               .andExpect(jsonPath("$.genre").value("Drama"));
    }

    // Test for "POST /movies/update/{movieTitle}"
    @Test
    public void testUpdateMovie() throws Exception {
        String movieTitle = "Movie1";
        Movie newMovie = new Movie("Movie3", "Drama", 130, 8.0, 2022);

        // Mock repository behavior for existing movie and title check
        Movie existingMovie = new Movie("Movie1", "Action", 120, 7.5, 2020); // Example existing movie
        existingMovie.setId(1L); // Mock existing ID
        
        when(movieRepository.existsByTitle(existingMovie.getTitle())).thenReturn(true);
        when(movieRepository.findByTitle(movieTitle)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.existsByTitle(newMovie.getTitle())).thenReturn(false);

        // Mocking the save behavior
        when(movieRepository.save(newMovie)).thenReturn(newMovie);

        // Perform the update operation
        mockMvc.perform(post("/movies/update/{movieTitle}", movieTitle)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Movie3\", \"genre\": \"Drama\", \"duration\": 130, \"rating\": 8.0, \"releaseYear\": 2022}"))
            .andExpect(status().isOk());
    }

    // Test for "DELETE /movies/{movieTitle}"
    @Test
    public void testDeleteMovie() throws Exception {
        String movieTitle = "Movie1";

        when(movieRepository.existsByTitle(movieTitle)).thenReturn(true);

        mockMvc.perform(delete("/movies/{movieTitle}", movieTitle))
               .andExpect(status().isOk());
    }

    // Test for movie already exists while creating (should return 400)
    @Test
    public void testCreateMovieWithDuplicateTitle() throws Exception {
        Movie newMovie = new Movie("Movie1", "Action", 120, 7.5, 2021);
        when(movieRepository.existsByTitle(newMovie.getTitle())).thenReturn(true);

        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Movie1\", \"genre\": \"Action\", \"duration\": 120, \"rating\": 7.5, \"releaseYear\": 2021}"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("A movie with the title 'Movie1' already exists."));
    }

    // Test for movie not found while updating (should return 404)
    @Test
    public void testUpdateMovieNotFound() throws Exception {
        String movieTitle = "MovieNotFound";
        when(movieRepository.existsByTitle(movieTitle)).thenReturn(false);

        mockMvc.perform(post("/movies/update/{movieTitle}", movieTitle)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Movie3\", \"genre\": \"Drama\", \"duration\": 130, \"rating\": 8.0, \"releaseYear\": 2022}"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Movie with title 'MovieNotFound' not found."));
    }
}
