package com.example.hotelroomallocation.controller;

import com.example.hotelroomallocation.model.BookingRequest;
import com.example.hotelroomallocation.model.BookingResponse;
import com.example.hotelroomallocation.service.BookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room-allocation")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/occupancy")
    public BookingResponse optimizeOccupancy(@RequestBody BookingRequest bookingRequest) {
        return bookingService.optimizeOccupancy(bookingRequest);
    }
}
