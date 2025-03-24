package com.att.tdp.popcorn_palace.booking;

import java.util.UUID;

public class BookingResponseDTO {

    private UUID bookingId;

    // Constructor
    public BookingResponseDTO(UUID bookingId) {
        this.bookingId = bookingId;
    }

    // Getter and Setter
    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }
}

