package com.example.hotelroomallocation.service;

import com.example.hotelroomallocation.model.BookingRequest;
import com.example.hotelroomallocation.model.BookingResponse;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    public BookingResponse optimizeOccupancy(BookingRequest bookingRequest) {
        return new BookingResponse();
    }
}
