package com.example.hotelroomallocation.service;

import com.example.hotelroomallocation.model.BookingRequest;
import com.example.hotelroomallocation.model.BookingResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class BookingService {

    public BookingResponse optimizeOccupancy(BookingRequest bookingRequest) {
        int availableEconomyRooms = bookingRequest.getEconomyRooms();
        int availablePremiumRooms = bookingRequest.getPremiumRooms();
        List<Double> potentialGuests =  bookingRequest.getPotentialGuests();

        List<Double> potentialEconomyGuests = new ArrayList<>(filterGuests(potentialGuests, isWillingToPayAtLeast(100.00).negate()));
        List<Double> potentialPremiumGuests = new ArrayList<>(filterGuests(potentialGuests, isWillingToPayAtLeast(100.00)));

        int overbookedEconomyGuests = potentialEconomyGuests.size() - availableEconomyRooms;
        int availablePremiumSpace = availablePremiumRooms - potentialPremiumGuests.size();

        if (overbookedEconomyGuests > 0 && availablePremiumSpace > 0) {
            int numberOfEconomyGuestsToUpgrade = Math.min(overbookedEconomyGuests, availablePremiumSpace);
            List<Double> economyGuestsToUpgrade = new ArrayList<>();
            for (int i = 0; i < numberOfEconomyGuestsToUpgrade; i++) {
                economyGuestsToUpgrade.add(potentialEconomyGuests.get(i));
            }
            potentialPremiumGuests.addAll(economyGuestsToUpgrade);
            potentialEconomyGuests.removeAll(economyGuestsToUpgrade);
        }

        double revenueEconomy = calculateRevenue(potentialEconomyGuests);

        double revenuePremium = calculateRevenue(potentialPremiumGuests);

        return new BookingResponse(potentialPremiumGuests.size(), revenuePremium, potentialEconomyGuests.size(), revenueEconomy);
    }

    private List<Double> filterGuests(List<Double> potentialGuests, Predicate<Double> predicate) {
        return potentialGuests.stream()
                .filter(predicate)
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    private Predicate<Double> isWillingToPayAtLeast(double maxAmount) {
        return amount -> amount >= maxAmount;
    }

    private double calculateRevenue(List<Double> guests) {
        return guests.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

}