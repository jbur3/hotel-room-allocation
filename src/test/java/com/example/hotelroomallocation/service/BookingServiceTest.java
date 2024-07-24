package com.example.hotelroomallocation.service;

import com.example.hotelroomallocation.model.BookingRequest;
import com.example.hotelroomallocation.model.BookingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingServiceTest {

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
    }

    @Test
    void whenBookingRequestIsReceived_itIsOptimized() {
        // GIVEN
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setPremiumRooms(7);
        bookingRequest.setEconomyRooms(5);
        bookingRequest.setPotentialGuests(Arrays.asList(23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0));

        // WHEN
        BookingResponse bookingResponse = bookingService.optimizeOccupancy(bookingRequest);

        // THEN
        assertThat(bookingResponse.getUsagePremium()).isEqualTo(6);
        assertThat(bookingResponse.getRevenuePremium()).isEqualTo(1054);
        assertThat(bookingResponse.getUsageEconomy()).isEqualTo(4);
        assertThat(bookingResponse.getRevenueEconomy()).isEqualTo(189.99);
    }
}
