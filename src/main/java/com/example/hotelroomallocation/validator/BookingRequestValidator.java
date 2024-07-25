package com.example.hotelroomallocation.validator;

import com.example.hotelroomallocation.exception.HotelRoomAllocationException;
import com.example.hotelroomallocation.model.BookingRequest;
import org.springframework.stereotype.Component;

@Component
public class BookingRequestValidator {

    public void validate(BookingRequest bookingRequest) {
        if (bookingRequest == null)
            throw new HotelRoomAllocationException("Booking request cannot be null");

        if (bookingRequest.getEconomyRooms() < 0)
            throw new HotelRoomAllocationException("Economy rooms number cannot be negative");

        if (bookingRequest.getPremiumRooms() < 0)
            throw new HotelRoomAllocationException("Premium rooms number cannot be negative");

        if (bookingRequest.getPotentialGuests() == null)
            throw new HotelRoomAllocationException("List of potential guests cannot be null");

        for (Double amount : bookingRequest.getPotentialGuests()) {
            if (amount == null || amount < 0)
                throw new HotelRoomAllocationException("Amount which guest is willing to pay cannot be null or negative");
        }
    }
}
