package com.example.hotelroomallocation.validator;

import com.example.hotelroomallocation.exception.HotelRoomAllocationException;
import com.example.hotelroomallocation.model.BookingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingRequestValidatorTest {

    private BookingRequestValidator validator;
    private BookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        validator = new BookingRequestValidator();
        bookingRequest = new BookingRequest();
    }

    @Test
    void whenNullBookingRequestIsReceived_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> validator.validate(null));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Booking request cannot be null");
    }

    @Test
    void whenBookingRequestWithNegativeEconomyRoomsIsReceived_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN
        bookingRequest.setPremiumRooms(2);
        bookingRequest.setEconomyRooms(-1);

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> validator.validate(bookingRequest));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Economy rooms number cannot be negative");
    }

    @Test
    void whenBookingRequestWithNegativePremiumRoomsIsReceived_HotelRoomAllocationExceptionIsThrown() {
        // GIVEN
        bookingRequest.setPremiumRooms(-1);
        bookingRequest.setEconomyRooms(2);

        // WHEN
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> validator.validate(bookingRequest));

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
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> validator.validate(bookingRequest));

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
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> validator.validate(bookingRequest));

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
        Exception exception = assertThrows(HotelRoomAllocationException.class, () -> validator.validate(bookingRequest));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Amount which guest is willing to pay cannot be null or negative");
    }
}
