package com.att.tdp.popcorn_palace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.att.tdp.popcorn_palace.booking.Booking;
import com.att.tdp.popcorn_palace.booking.BookingRepository;
import com.att.tdp.popcorn_palace.movie.Movie;
import com.att.tdp.popcorn_palace.movie.MovieRepository;
import com.att.tdp.popcorn_palace.showtime.Showtime;
import com.att.tdp.popcorn_palace.showtime.ShowtimeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
        assertNotNull(movies);
        assertEquals(5, movies.size()); // Validate the number of movies
    }

    @Test
    public void testGetAllMoviesWhenNoMoviesExist() {
        movieRepository.deleteAll();  // Clear all movies
        List<Movie> movies = restClient.get().uri("/movies/all").retrieve().body(new ParameterizedTypeReference<>() {});
        assertNotNull(movies);
        assertEquals(0, movies.size()); // Verify no movies
    }

    private void assertMovieJson(Movie expected, JsonNode actual) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            assertTrue(actual.has("id"));
            Long id = actual.get("id").asLong();
            Movie movie = objectMapper.readValue(actual.toString(), Movie.class);

            if (expected.getId() != null) {
                assertEquals(expected.getId(), id);
            }
            assertEquals(expected.getTitle(), movie.getTitle());
            assertEquals(expected.getGenre(), movie.getGenre());
            assertEquals(expected.getDuration(), movie.getDuration());
            assertEquals(expected.getRating(), movie.getRating(), 0.1);
            assertEquals(expected.getReleaseYear(), movie.getReleaseYear());
        } catch (JsonProcessingException e) {
            fail("Failed to process JSON: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllMoviesDetails() {
        String jsonResponse = restClient.get().uri("/movies/all?size=1&sort=title,desc").retrieve().body(String.class);
        assertNotNull(jsonResponse);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            assertTrue(jsonNode.isArray());
            JsonNode firstMovie = jsonNode.get(0);
            assertMovieJson(shawshank, firstMovie);
        } catch (JsonProcessingException e) {
            fail("Failed to process JSON: " + e.getMessage());
        }
    }

    @Test
    public void testCreateMovieSuccess() {
        Movie newMovie = new Movie("Interstellar", "SCI-FI", 169, 8.6, 2014);
        String jsonResponse = restClient.post().uri("/movies")
        .body(newMovie).retrieve().toEntity(String.class).getBody();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            assertMovieJson(newMovie, jsonNode);
        } catch (JsonProcessingException e) {
            fail("Failed to process JSON: " + e.getMessage());
        }
    }

    @Test
    public void testCreateMovieDuplicate() {
        Movie duplicateMovie = new Movie(inception.getTitle(), "SCI-FI", 148, 8.8, 2010);
        ResponseEntity<String> res = restClient.post().uri("/movies").body(duplicateMovie).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("A movie with the title 'Inception' already exists.", errorMessage);
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
    public void testUpdateMovieSameTitle() {
        String oldTitle = shawshank.getTitle();
        shawshank.setReleaseYear(2010);
        ResponseEntity<Void> response = restClient.post().uri("/movies/update/{movieTitle}", oldTitle).body(shawshank).retrieve().toBodilessEntity();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2010, movieRepository.findById(shawshank.getId()).get().getReleaseYear());
    }

    @Test
    public void testUpdateMovieNotExist() {
        ResponseEntity<String> res = restClient.post().uri("/movies/update/{movieTitle}", "NoMovie").body(shawshank).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("Movie with title 'NoMovie' not found.", errorMessage);
    }

    @Test
    public void testUpdateMovieDuplicate() {
        Movie duplicateMovie = new Movie(inception.getTitle(), "SCI-FI", 148, 8.8, 2010);
        ResponseEntity<String> res = restClient.post().uri("/movies/update/{movieTitle}", shawshank.getTitle()).body(duplicateMovie).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("A movie with the title 'Inception' already exists.", errorMessage);
    }

    @Test
    public void testDeleteMovieSuccess() {
        ResponseEntity<Void> response = restClient.delete().uri("/movies/{movieTitle}", matrix.getTitle()).retrieve().toBodilessEntity();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(movieRepository.existsById(matrix.getId()));
    }

    @Test
    public void testDeleteMovieNotExist() {
        ResponseEntity<String> res = restClient.delete().uri("/movies/{movieTitle}", "NoMovie").retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    /****************************** Showtime API ******************************/

    private void assertShowtimeJson(Showtime expected, String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            assertTrue(jsonNode.has("id"));
            Long id = jsonNode.get("id").asLong();
            Showtime actual = objectMapper.readValue(jsonNode.toString(), Showtime.class);
            
            if (expected.getId() != null) {
                assertEquals(expected.getId(), id);
            }
            assertEquals(expected.getPrice(), actual.getPrice(), 0.1);
            assertEquals(expected.getTheater(), actual.getTheater());
            assertEquals(expected.getStartTime(), actual.getStartTime());
            assertEquals(expected.getEndTime(), actual.getEndTime());
        } catch (JsonProcessingException e) {
            fail("Failed to process JSON: " + e.getMessage());
        }
    }

    @Test
    public void testGetShowtimeByIdSuccess() {
        String jsonResponse = restClient.get().uri("/showtimes/{showtimeId}", showtime1.getId()).retrieve().body(String.class);
        assertNotNull(jsonResponse);
        assertShowtimeJson(showtime1, jsonResponse);
    }

    @Test
    public void testGetShowtimeByIdNotExist() {
        ResponseEntity<String> res = restClient.get().uri("/showtimes/{showtimeId}", 99999L).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("Showtime with id '99999' not found.", errorMessage);
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
        
        String jsonResponse = restClient.post().uri("/showtimes")
        .body(newShowtime).retrieve().toEntity(String.class).getBody();
        assertShowtimeJson(newShowtime, jsonResponse);
    }

    @Test
    public void testCreateShowtimeOverlap() {
        Showtime overlapShowtime = new Showtime(
            matrix.getId(),
            20.5,
            showtime1.getTheater(),
            showtime1.getStartTime(),
            showtime1.getEndTime()
        );

        ResponseEntity<String> res = restClient.post().uri("/showtimes").body(overlapShowtime).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("Overlapping showtime.", errorMessage);
    }

    @Test
    public void testUpdateShowtimeSuccess() {
        showtime1.setTheater("newTheater");
        ResponseEntity<Void> response = restClient.post().uri("/showtimes/update/{showtimeId}", showtime1.getId()).body(showtime1).retrieve().toBodilessEntity();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newTheater", showtimeRepository.findById(showtime1.getId()).get().getTheater());
    }

    @Test
    public void testUpdateShowtimeNotExist() {
        ResponseEntity<String> res = restClient.post().uri("/showtimes/update/{showtimeId}", 99999L).body(showtime1).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("Showtime with id '99999' not found.", errorMessage);
    }

    @Test
    public void testUpdateShowtimeSelfOverlap() {
        Showtime selfOverlapShowtime = new Showtime(
            darkKnight.getId(),
            50.70,
            showtime3.getTheater(),
            showtime3.getStartTime(),
            showtime3.getEndTime()
        );
        ResponseEntity<Void> response = restClient.post().uri("/showtimes/update/{showtimeId}", showtime3.getId()).body(selfOverlapShowtime).retrieve().toBodilessEntity();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(darkKnight.getId(), showtimeRepository.findById(showtime3.getId()).get().getMovieId());
    }

    @Test
    public void testUpdateShowtimeNoMovie() {
        Showtime noMovieShowtime = new Showtime(
            55555L,
            16.0,
            "IMAX Theater",
            LocalDateTime.parse("2025-03-10T15:30"),
            LocalDateTime.parse("2025-03-10T18:30")
        );
        ResponseEntity<String> res = restClient.post().uri("/showtimes/update/{showtimeId}", showtime1.getId()).body(noMovieShowtime).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("Movie with id '55555' does not exist.", errorMessage);
    }

    @Test
    public void testDeleteShowtimeSuccess() {
        ResponseEntity<Void> response = restClient.delete().uri("/showtimes/{showtimeId}", showtime2.getId()).retrieve().toBodilessEntity();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(showtimeRepository.existsById(showtime2.getId()));
    }

    @Test
    public void testDeleteShowtimeNotExist() {
        ResponseEntity<String> res = restClient.delete().uri("/showtimes/{showtimeId}", 99999L).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    /****************************** Booking API ******************************/

    @Test
    public void testCreateBookingSuccess() {
        Booking newBooking = new Booking(showtime3.getId(), 3, UUID.randomUUID());
        String jsonResponse = restClient.post().uri("/bookings")
        .body(newBooking).retrieve().toEntity(String.class).getBody();
        assertNotNull(jsonResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            assertTrue(jsonNode.has("bookingId"));
            UUID id = UUID.fromString(jsonNode.get("bookingId").asText());
            assertNotNull(id);
        } catch (JsonProcessingException e) {
            fail("Failed to process JSON: " + e.getMessage());
        }
    }

    @Test
    public void testCreateBookingShowtimeNotExist() {
        Booking newBooking = new Booking(99999L, 3, UUID.randomUUID());
        ResponseEntity<String> res = restClient.post().uri("/bookings").body(newBooking).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("Showtime with id '99999' does not exist.", errorMessage);
    }

    @Test
    public void testCreateBookingBookedSeat() {
        Booking newBooking = new Booking(book1.getShowtimeId(), book1.getSeatNumber(), UUID.randomUUID());
        ResponseEntity<String> res = restClient.post().uri("/bookings").body(newBooking).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
        .toEntity(String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        String errorMessage = res.getBody();
        assertNotNull(errorMessage);
        assertEquals("Seat Already Booked.", errorMessage);
    }

}
