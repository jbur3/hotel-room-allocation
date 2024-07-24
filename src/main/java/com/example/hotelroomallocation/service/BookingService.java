package com.example.hotelroomallocation.service;

import com.example.hotelroomallocation.model.BookingRequest;
import com.example.hotelroomallocation.model.BookingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    public BookingResponse optimizeOccupancy(BookingRequest bookingRequest) {
        List<Double> potentialGuests =  bookingRequest.getPotentialGuests();

        List<Double> potentialPremiumGuests = potentialGuests.stream()
                .filter(guest -> guest >= 100.00)
                .toList();

        double revenuePremium = potentialPremiumGuests.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        List<Double> potentialEconomyGuests = potentialGuests.stream()
                .filter(guest -> guest < 100.00)
                .toList();

        double revenueEconomy = potentialEconomyGuests.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        BookingResponse bookingResponse = new BookingResponse();

        bookingResponse.setUsagePremium(potentialPremiumGuests.size());
        bookingResponse.setRevenuePremium(revenuePremium);
        bookingResponse.setUsageEconomy(potentialEconomyGuests.size());
        bookingResponse.setRevenueEconomy(revenueEconomy);

        return bookingResponse;
    }
}
