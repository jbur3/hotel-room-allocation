package com.example.hotelroomallocation.service;

import com.example.hotelroomallocation.exception.HotelRoomAllocationException;
import com.example.hotelroomallocation.model.BookingRequest;
import com.example.hotelroomallocation.model.BookingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingServiceTest {

    private BookingService bookingService;
    private BookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
        bookingRequest = new BookingRequest();
        bookingRequest.setPotentialGuests(Arrays.asList(23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0));
    }

    @Test
    void whenBookingRequestIsReceived_itIsOptimized() {
        // GIVEN
        bookingRequest.setPremiumRooms(7);
        bookingRequest.setEconomyRooms(5);

        // WHEN
        BookingResponse bookingResponse = bookingService.optimizeOccupancy(bookingRequest);

        // THEN
        assertThat(bookingResponse.getUsagePremium()).isEqualTo(6);
        assertThat(bookingResponse.getRevenuePremium()).isEqualTo(1054);
        assertThat(bookingResponse.getUsageEconomy()).isEqualTo(4);
        assertThat(bookingResponse.getRevenueEconomy()).isEqualTo(189.99);
    }

    @Test
    void whenBookingRequestIsReceived_andThereIsFewerEconomyRoomsThanPotentialEconomyGuests_itIsOptimized() {
        // GIVEN
        bookingRequest.setPremiumRooms(9);
        bookingRequest.setEconomyRooms(4);
        bookingRequest.setPotentialGuests(Arrays.asList(23.0, 45.0, 155.0, 374.0, 22.0, 35.0, 99.99, 100.0, 101.0, 115.0, 209.0, 77.8));

        // WHEN
        BookingResponse bookingResponse = bookingService.optimizeOccupancy(bookingRequest);

        // THEN
        assertThat(bookingResponse.getUsagePremium()).isEqualTo(8);
        assertThat(bookingResponse.getRevenuePremium()).isEqualTo(1231.79);
        assertThat(bookingResponse.getUsageEconomy()).isEqualTo(4);
        assertThat(bookingResponse.getRevenueEconomy()).isEqualTo(125);
    }

    @Test
    void whenBookingRequestIsReceived_andThereIsFewerRoomsThanPotentialGuests_itIsOptimized() {
        // GIVEN
        bookingRequest.setPremiumRooms(3);
        bookingRequest.setEconomyRooms(3);

        // WHEN
        BookingResponse bookingResponse = bookingService.optimizeOccupancy(bookingRequest);

        // THEN
        assertThat(bookingResponse.getUsagePremium()).isEqualTo(3);
        assertThat(bookingResponse.getRevenuePremium()).isEqualTo(738);
        assertThat(bookingResponse.getUsageEconomy()).isEqualTo(3);
        assertThat(bookingResponse.getRevenueEconomy()).isEqualTo(167.99);
    }

    @Test
    void whenBookingRequestIsReceived_andThereIsFewerPremiumRoomsThanPotentialPremiumGuests_itIsOptimized() {
        // GIVEN
        bookingRequest.setPremiumRooms(2);
        bookingRequest.setEconomyRooms(7);

        // WHEN
        BookingResponse bookingResponse = bookingService.optimizeOccupancy(bookingRequest);

        // THEN
        assertThat(bookingResponse.getUsagePremium()).isEqualTo(2);
        assertThat(bookingResponse.getRevenuePremium()).isEqualTo(583);
        assertThat(bookingResponse.getUsageEconomy()).isEqualTo(4);
        assertThat(bookingResponse.getRevenueEconomy()).isEqualTo(189.99);
    }

    @Test
    void whenNullBookingRequestIsReceived_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> bookingService.optimizeOccupancy(null));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Booking request cannot be null");
    }

    @Test
    void whenBookingRequestWithNegativeEconomyRoomsIsReceived_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN
        bookingRequest.setPremiumRooms(2);
        bookingRequest.setEconomyRooms(-1);

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> bookingService.optimizeOccupancy(bookingRequest));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Economy rooms number cannot be negative");
    }

    @Test
    void whenBookingRequestWithNegativePremiumRoomsIsReceived_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN
        bookingRequest.setPremiumRooms(-1);
        bookingRequest.setEconomyRooms(2);

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> bookingService.optimizeOccupancy(bookingRequest));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Premium rooms number cannot be negative");
    }

    @Test
    void whenBookingRequestWithNullPotentialGuestsListIsReceived_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN
        bookingRequest.setPremiumRooms(1);
        bookingRequest.setEconomyRooms(2);
        bookingRequest.setPotentialGuests(null);

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> bookingService.optimizeOccupancy(bookingRequest));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("List of potential guests cannot be null");
    }

    @Test
    void whenBookingRequestIsReceived_andOneOfPotentialGuestsIsNull_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN
        bookingRequest.setPremiumRooms(1);
        bookingRequest.setEconomyRooms(2);
        bookingRequest.setPotentialGuests(Arrays.asList(24.00, 134.99, null));

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> bookingService.optimizeOccupancy(bookingRequest));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Amount which guest is willing to pay cannot be null or negative");
    }

    @Test
    void whenBookingRequestIsReceived_andOneOfPotentialGuestsIsNegative_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN
        bookingRequest.setPremiumRooms(1);
        bookingRequest.setEconomyRooms(2);
        bookingRequest.setPotentialGuests(Arrays.asList(24.00, 134.99, -16.00));

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> bookingService.optimizeOccupancy(bookingRequest));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Amount which guest is willing to pay cannot be null or negative");
    }
}
