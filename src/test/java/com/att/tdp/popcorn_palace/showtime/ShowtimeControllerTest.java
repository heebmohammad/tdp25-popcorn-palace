package com.att.tdp.popcorn_palace.showtime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.att.tdp.popcorn_palace.movie.MovieRepository;

@WebMvcTest(ShowtimeController.class)
public class ShowtimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowtimeRepository showtimeRepository;

    @MockitoBean
    private MovieRepository movieRepository;

    // Test for "GET /showtimes/{showtimeId}"
    @Test
    public void testFindShowtimeById() throws Exception {
        Long showtimeId = 1L;
        Showtime showtime = new Showtime(1L, 10.5, "Theater1", LocalDateTime.of(2025, 3, 24, 10, 0), LocalDateTime.of(2025, 3, 24, 12, 0));
        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(showtime));

        mockMvc.perform(get("/showtimes/{showtimeId}", showtimeId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.theater").value("Theater1"));
    }

    // Test for "GET /showtimes/{showtimeId}" when showtime is not found
    @Test
    public void testFindShowtimeByIdNotFound() throws Exception {
        Long showtimeId = 999L;  // A showtimeId that doesn't exist
        when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/showtimes/{showtimeId}", showtimeId))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Showtime with id '999' not found."));
    }

    // Test for "POST /showtimes" - Creating a showtime
    @Test
    public void testCreateShowtime() throws Exception {
        Showtime newShowtime = new Showtime(1L, 10.5, "Theater1", LocalDateTime.of(2025, 3, 24, 10, 0), LocalDateTime.of(2025, 3, 24, 12, 0));
        
        when(movieRepository.existsById(1L)).thenReturn(true);
        when(showtimeRepository.isOverlappingShowtime(1L, "Theater1", newShowtime.getStartTime(), newShowtime.getEndTime())).thenReturn(false);
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(newShowtime);

        mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\": 1, \"theater\": \"Theater1\", \"startTime\": \"2025-03-24T10:00:00\", \"endTime\": \"2025-03-24T12:00:00\", \"price\": 20.0}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.theater").value("Theater1"));
    }

    // Test for "POST /showtimes" when movie does not exist
    @Test
    public void testCreateShowtimeWithNonExistingMovie() throws Exception {
        when(movieRepository.existsById(999L)).thenReturn(false);

        mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\": 999, \"theater\": \"Theater1\", \"startTime\": \"2025-03-24T10:00:00\", \"endTime\": \"2025-03-24T12:00:00\", \"price\": 20.0}"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("Movie with id '999' does not exist."));
    }

    // Test for "POST /showtimes" when showtime overlaps
    @Test
    public void testCreateShowtimeWithOverlap() throws Exception {
        Showtime newShowtime = new Showtime(1L, 10.5, "Theater1", LocalDateTime.of(2025, 3, 24, 10, 0), LocalDateTime.of(2025, 3, 24, 12, 0));

        when(movieRepository.existsById(1L)).thenReturn(true);
        when(showtimeRepository.isOverlappingShowtime(null, "Theater1", newShowtime.getStartTime(), newShowtime.getEndTime())).thenReturn(true);

        mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\": 1, \"theater\": \"Theater1\", \"startTime\": \"2025-03-24T10:00:00\", \"endTime\": \"2025-03-24T12:00:00\", \"price\": 20.0}"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("Overlapping showtime."));
    }

    // Test for "POST /showtimes/update/{showtimeId}" - Updating a showtime
    @Test
    public void testUpdateShowtime() throws Exception {
        Long showtimeId = 1L;
        Showtime updatedShowtime = new Showtime(1L, 10.5, "Theater1", LocalDateTime.of(2025, 3, 24, 10, 0), LocalDateTime.of(2025, 3, 24, 12, 0));

        when(showtimeRepository.existsById(showtimeId)).thenReturn(true);
        when(movieRepository.existsById(1L)).thenReturn(true);
        when(showtimeRepository.isOverlappingShowtime(1L, "Theater1", updatedShowtime.getStartTime(), updatedShowtime.getEndTime())).thenReturn(false);

        mockMvc.perform(post("/showtimes/update/{showtimeId}", showtimeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\": 1, \"theater\": \"Theater1\", \"startTime\": \"2025-03-24T10:00:00\", \"endTime\": \"2025-03-24T12:00:00\", \"price\": 20.0}"))
               .andExpect(status().isOk());
    }

    // Test for "POST /showtimes/update/{showtimeId}" when showtime to update doesn't exist
    @Test
    public void testUpdateShowtimeNotFound() throws Exception {
        Long showtimeId = 999L;
        when(showtimeRepository.existsById(showtimeId)).thenReturn(false);

        mockMvc.perform(post("/showtimes/update/{showtimeId}", showtimeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\": 1, \"theater\": \"Theater1\", \"startTime\": \"2025-03-24T10:00:00\", \"endTime\": \"2025-03-24T12:00:00\", \"price\": 20.0}"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Showtime with id '999' not found."));
    }

    // Test for "DELETE /showtimes/{showtimeId}" - Deleting a showtime
    @Test
    public void testDeleteShowtime() throws Exception {
        Long showtimeId = 1L;

        when(showtimeRepository.existsById(showtimeId)).thenReturn(true);

        mockMvc.perform(delete("/showtimes/{showtimeId}", showtimeId))
               .andExpect(status().isOk());
    }

    // Test for "DELETE /showtimes/{showtimeId}" when showtime is not found
    @Test
    public void testDeleteShowtimeNotFound() throws Exception {
        Long showtimeId = 999L;

        when(showtimeRepository.existsById(showtimeId)).thenReturn(false);

        mockMvc.perform(delete("/showtimes/{showtimeId}", showtimeId))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Showtime with id '999' not found."));
    }
}
