package com.att.tdp.popcorn_palace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import com.att.tdp.popcorn_palace.booking.Booking;
import com.att.tdp.popcorn_palace.booking.BookingRepository;
import com.att.tdp.popcorn_palace.movie.Movie;
import com.att.tdp.popcorn_palace.movie.MovieRepository;
import com.att.tdp.popcorn_palace.showtime.Showtime;
import com.att.tdp.popcorn_palace.showtime.ShowtimeRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PopcornPalaceIntegrationTest {

    @LocalServerPort
    private int port; // The random port assigned to the application during testing

    private RestClient restClient;

    // Declare variables to be accessible in test methods
    private Movie shawshank;
    private Movie darkKnight;
    private Movie forrestGump;
    private Movie inception;
    private Movie matrix;
    
    private Showtime showtime1;
    private Showtime showtime2;
    private Showtime showtime3;

    private Booking book1;
    private Booking book2;
    private Booking book3;

    @Autowired
    private MovieRepository movieRepository; // Assume this is your JPA repository for Movie
    @Autowired
    private ShowtimeRepository showtimeRepository; // Assume this is your JPA repository for Showtime
    @Autowired
    private BookingRepository bookingRepository; // Assume this is your JPA repository for Booking

    @BeforeEach
    public void setup() {
        restClient = RestClient.create("http://localhost:" + port);

        // Clear Movie, Showtime, and Booking tables
        movieRepository.deleteAll();
        showtimeRepository.deleteAll();
        bookingRepository.deleteAll();

        // Insert Movies
        shawshank = new Movie("The Shawshank Redemption", "DRAMA", 142, 9.3, 1994);
        darkKnight = new Movie("The Dark Knight", "ACTION", 152, 9.0, 2008);
        forrestGump = new Movie("Forrest Gump", "DRAMA", 142, 8.8, 1994);
        inception = new Movie("Inception", "SCI-FI", 148, 8.8, 2010);
        matrix = new Movie("The Matrix", "SCI-FI", 136, 8.7, 1999);
        movieRepository.saveAll(List.of(shawshank, darkKnight, forrestGump, inception, matrix));

        // Insert Showtimes
        showtime1 = new Showtime(
            shawshank.getId(),
            12.0,
            "AMC Theater",
            LocalDateTime.parse("2025-02-14T11:47"),
            LocalDateTime.parse("2025-02-14T14:47")
        );
        showtime2 = new Showtime(
            darkKnight.getId(),
            15.0,
            "Regal Cinema",
            LocalDateTime.parse("2025-02-14T12:00"),
            LocalDateTime.parse("2025-02-14T14:30")
        );
        showtime3 = new Showtime(
            forrestGump.getId(),
            14.0,
            "Cinemark",
            LocalDateTime.parse("2025-02-15T09:30"),
            LocalDateTime.parse("2025-02-15T12:00")
        );
        showtimeRepository.saveAll(List.of(showtime1, showtime2, showtime3));

        // Insert Bookings
        book1 = new Booking(showtime1.getId(), 10, UUID.randomUUID());
        book2 = new Booking(showtime2.getId(), 15, UUID.randomUUID());
        book3 = new Booking(showtime3.getId(), 5, UUID.randomUUID());
        bookingRepository.saveAll(List.of(book1, book2, book3));
    }

    /****************************** Movies API ******************************/
    @Test
    public void testGetAllMovies() {
        List<Movie> movies = restClient.get().uri("/movies/all").retrieve().body(new ParameterizedTypeReference<>() {});
        assertEquals(5, movies.size()); // Validate the number of movies
    }

    @Test
    public void testGetAllMoviesWhenNoMoviesExist() {
        movieRepository.deleteAll();  // Clear all movies
        List<Movie> movies = restClient.get().uri("/movies/all").retrieve().body(new ParameterizedTypeReference<>() {});
        assertEquals(0, movies.size()); // Verify no movies
    }

    @Test
    public void testGetAllMoviesDetails() {
        List<Movie> movies = restClient.get().uri("/movies/all").retrieve().body(new ParameterizedTypeReference<>() {});
        
        // Assert that the movie details are correct
        assertEquals("The Shawshank Redemption", movies.getFirst().getTitle());
        assertEquals("DRAMA", movies.getFirst().getGenre().toString());
        assertEquals(142, movies.getFirst().getDuration());
        assertEquals(9.3, movies.getFirst().getRating(), 0.1);
        assertEquals(1994, movies.getFirst().getReleaseYear());
    }

    @Test
    public void testCreateMovieSuccess() {
        Movie newMovie = new Movie("Interstellar", "SCI-FI", 169, 8.6, 2014);
        ResponseEntity<Movie> response = restClient.post().uri("/movies").body(newMovie).retrieve().toEntity(Movie.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(movieRepository.findByTitle("Interstellar"));
    }

    @Test
    public void testCreateMovieDuplicate() {
        Movie duplicateMovie = new Movie("Inception", "SCI-FI", 148, 8.8, 2010);
        ResponseEntity<String> res = restClient.post().uri("/movies").body(duplicateMovie).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertTrue(res.getBody().contains("A movie with the title 'Inception' already exists."));
    }

    @Test
    public void testUpdateMovieSuccess() {
        String oldTitle = shawshank.getTitle();
        shawshank.setTitle("The Shawshank Redemption 2");
        ResponseEntity<Void> response = restClient.post().uri("/movies/update/{movieTitle}", oldTitle).body(shawshank).retrieve().toBodilessEntity();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The Shawshank Redemption 2", movieRepository.findById(shawshank.getId()).get().getTitle());
    }

    @Test
    public void testDeleteMovieSuccess() {
        ResponseEntity<Void> response = restClient.delete().uri("/movies/{movieTitle}", matrix.getTitle()).retrieve().toBodilessEntity();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(movieRepository.existsById(matrix.getId()));
    }

    /****************************** Showtime API ******************************/

/*    @Test
    public void testGetShowtimeByIdSuccess() {
        ResponseEntity<Showtime> response = restClient.get("/showtimes/" + showtime1.getId(), Showtime.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(showtime1.getId(), response.getBody().getId());
    }

    @Test
    public void testCreateShowtimeSuccess() {
        Showtime newShowtime = new Showtime(
            inception.getId(),
            16.0,
            "IMAX Theater",
            LocalDateTime.parse("2025-03-10T15:30"),
            LocalDateTime.parse("2025-03-10T18:30")
        );
        ResponseEntity<Showtime> response = restClient.post("/showtimes", newShowtime, Showtime.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(showtimeRepository.findById(newShowtime.getId()));
    }

    @Test
    public void testDeleteShowtimeSuccess() {
        ResponseEntity<Void> response = restClient.delete("/showtimes/" + showtime2.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(showtimeRepository.existsById(showtime2.getId()));
    }

*/
    /****************************** Booking API ******************************/
/*
    @Test
    public void testCreateBookingSuccess() {
        Booking newBooking = new Booking(showtime3.getId(), 3, UUID.randomUUID());
        ResponseEntity<Booking> response = restClient.post("/bookings", newBooking, Booking.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(bookingRepository.findById(newBooking.getId()));
    }

    @Test
    public void testCreateBookingExceedsCapacity() {
        Booking exceedCapacityBooking = new Booking(showtime1.getId(), 100, UUID.randomUUID());
        ResponseEntity<String> response = restClient.post("/bookings", exceedCapacityBooking, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Exceeds capacity"));
    }
*/
}
