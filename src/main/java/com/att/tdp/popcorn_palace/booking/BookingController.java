package com.att.tdp.popcorn_palace.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.showtime.ShowtimeRepository;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    BookingController(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    private ResponseEntity<?> checkIfSeatAlreadyBooked(Booking booking) {
        if (bookingRepository.isSeatAlreadyBooked(
            booking.getShowtimeId(), booking.getSeatNumber())) {
                return ResponseEntity.badRequest().body("Seat Already Booked.");
        }
        return null; // No error found
    }
    
    // Helper method to check if a showtime exist by id
    private ResponseEntity<?> checkIfShowtimeExist(Long showtimeId) {
        if (!showtimeRepository.existsById(showtimeId)) {
            return ResponseEntity.badRequest()
                .body("Showtime with id '" + showtimeId + "' does not exist.");
        }
        return null; // No error found
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Booking booking) {
        ResponseEntity<?> showtimeExistResponse = checkIfShowtimeExist(booking.getShowtimeId());
        if (showtimeExistResponse != null) {
            return showtimeExistResponse;
        }

        ResponseEntity<?> seatResponse = checkIfSeatAlreadyBooked(booking);
        if (seatResponse != null) {
            return seatResponse;
        }

        Booking savedBooking = bookingRepository.save(booking);

        // Return a 200 Ok response with the bookingId
        return ResponseEntity.ok(new BookingResponseDTO(savedBooking.getBookingId()));
    }
}
