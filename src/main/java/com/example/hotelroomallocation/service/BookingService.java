package com.example.hotelroomallocation.service;

import com.example.hotelroomallocation.exception.HotelRoomAllocationException;
import com.example.hotelroomallocation.model.BookingRequest;
import com.example.hotelroomallocation.model.BookingResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BookingService {

    public BookingResponse optimizeOccupancy(BookingRequest bookingRequest) {
        validateBookingRequest(bookingRequest);

        int availableEconomyRooms = bookingRequest.getEconomyRooms();
        int availablePremiumRooms = bookingRequest.getPremiumRooms();
        List<Double> potentialGuests =  bookingRequest.getPotentialGuests();

        List<Double> potentialEconomyGuests = new ArrayList<>(filterGuests(potentialGuests, isEconomyGuest()));
        List<Double> potentialPremiumGuests = new ArrayList<>(filterGuests(potentialGuests, isPremiumGuest()));

        List<Double> premiumGuestsToAllocate = allocatePremiumGuests(potentialPremiumGuests, availablePremiumRooms);
        List<Double> economyGuestsToAllocate = allocateAndUpgradeEconomyGuests(potentialEconomyGuests, potentialPremiumGuests, availableEconomyRooms, availablePremiumRooms - premiumGuestsToAllocate.size());

        double revenueEconomy = calculateRevenue(economyGuestsToAllocate);
        double revenuePremium = calculateRevenue(premiumGuestsToAllocate);

        return new BookingResponse(premiumGuestsToAllocate.size(), revenuePremium, economyGuestsToAllocate.size(), revenueEconomy);
    }

    private void validateBookingRequest(BookingRequest bookingRequest) {
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

    private List<Double> filterGuests(List<Double> potentialGuests, Predicate<Double> predicate) {
        return potentialGuests.stream()
                .filter(predicate)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private Predicate<Double> isEconomyGuest() {
        return amount -> amount < 100.00;
    }

    private Predicate<Double> isPremiumGuest() {
        return amount -> amount >= 100.00;
    }

    private List<Double> allocatePremiumGuests(List<Double> potentialPremiumGuests, int availablePremiumRooms) {
        if (availablePremiumRooms >= potentialPremiumGuests.size())
            return potentialPremiumGuests;

        List<Double> premiumGuestsToAllocate = new ArrayList<>();
        for (int i = 0; i < availablePremiumRooms; i++) {
            premiumGuestsToAllocate.add(potentialPremiumGuests.get(i));
        }
        return premiumGuestsToAllocate;
    }

    private List<Double> allocateAndUpgradeEconomyGuests(List<Double> potentialEconomyGuests, List<Double> premiumGuestsToAllocate, int availableEconomyRooms, int availablePremiumRooms) {
        if (availableEconomyRooms >= potentialEconomyGuests.size())
            return potentialEconomyGuests;

        List<Double> economyGuestsToAllocate = new ArrayList<>();

        if (availablePremiumRooms > 0) {
            int numberOfEconomyGuestsToUpgrade = Math.min(potentialEconomyGuests.size() - availableEconomyRooms, availablePremiumRooms);
            for (int i = numberOfEconomyGuestsToUpgrade; i < potentialEconomyGuests.size(); i++) {
                economyGuestsToAllocate.add(potentialEconomyGuests.get(i));
            }
            for (int i = 0; i < numberOfEconomyGuestsToUpgrade; i++) {
                premiumGuestsToAllocate.add(potentialEconomyGuests.get(i));
            }
        }
        else {
            int numberOfEconomyGuestsNotToAllocate = potentialEconomyGuests.size() - availableEconomyRooms;
            for (int i = 0; i < potentialEconomyGuests.size() - numberOfEconomyGuestsNotToAllocate; i++) {
                economyGuestsToAllocate.add(potentialEconomyGuests.get(i));
            }
        }

        return economyGuestsToAllocate;
    }

    private double calculateRevenue(List<Double> guests) {
        return guests.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}