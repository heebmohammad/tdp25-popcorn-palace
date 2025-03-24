package com.att.tdp.popcorn_palace.booking;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.att.tdp.popcorn_palace.showtime.ShowtimeRepository;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingRepository bookingRepository;

    @MockitoBean
    private ShowtimeRepository showtimeRepository;

    private final UUID USER_ID = UUID.fromString("84438967-f68f-4fa0-b620-0f08217e76af");

    // Test for successful booking creation
    @Test
    public void testCreateBooking() throws Exception {
        // Prepare test data
        Long showtimeId = 1L;
        Integer seatNumber = 15;

        // Mock repository responses
        when(showtimeRepository.existsById(showtimeId)).thenReturn(true);
        when(bookingRepository.isSeatAlreadyBooked(showtimeId, seatNumber)).thenReturn(false);

        // Mock the save method of the bookingRepository
        Booking savedBooking = new Booking(showtimeId, seatNumber, USER_ID);
        UUID id = UUID.randomUUID();
        savedBooking.setBookingId(id);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        // Perform POST request
        mockMvc.perform(post("/bookings")
                .contentType("application/json")
                .content("{\"showtimeId\": " + showtimeId + ", \"seatNumber\": \"" + seatNumber + "\", \"userId\": \"" + USER_ID.toString() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(id.toString()));
    }

    // Test when the seat is already booked (should return 400)
    @Test
    public void testSeatAlreadyBooked() throws Exception {
        Long showtimeId = 1L;
        Integer seatNumber = 15;

        // Mock repository responses
        when(showtimeRepository.existsById(showtimeId)).thenReturn(true);
        when(bookingRepository.isSeatAlreadyBooked(showtimeId, seatNumber)).thenReturn(true);

        // Perform POST request
        mockMvc.perform(post("/bookings")
                .contentType("application/json")
                .content("{\"showtimeId\": " + showtimeId + ", \"seatNumber\": \"" + seatNumber + "\", \"userId\": \"" + USER_ID.toString() + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Seat Already Booked."));
    }

    // Test when the showtime does not exist (should return 400)
    @Test
    public void testShowtimeDoesNotExist() throws Exception {
        Long showtimeId = 1L;
        Integer seatNumber = 15;

        // Mock repository responses
        when(showtimeRepository.existsById(showtimeId)).thenReturn(false);

        // Perform POST request
        mockMvc.perform(post("/bookings")
                .contentType("application/json")
                .content("{\"showtimeId\": " + showtimeId + ", \"seatNumber\": \"" + seatNumber + "\", \"userId\": \"" + USER_ID.toString() + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Showtime with id '1' does not exist."));
    }
}

