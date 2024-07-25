package com.example.hotelroomallocation.service;

import com.example.hotelroomallocation.model.BookingRequest;
import com.example.hotelroomallocation.model.BookingResponse;
import com.example.hotelroomallocation.validator.BookingRequestValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private BookingRequestValidator bookingRequestValidator;

    public BookingService(BookingRequestValidator bookingRequestValidator) {
        this.bookingRequestValidator = bookingRequestValidator;
    }

    public BookingResponse optimizeOccupancy(BookingRequest bookingRequest) {
        bookingRequestValidator.validate(bookingRequest);

        int availableEconomyRooms = bookingRequest.getEconomyRooms();
        int availablePremiumRooms = bookingRequest.getPremiumRooms();
        List<Double> potentialGuests = bookingRequest.getPotentialGuests();

        List<Double> potentialEconomyGuests = filterGuests(potentialGuests, isEconomyGuest());
        List<Double> potentialPremiumGuests = filterGuests(potentialGuests, isPremiumGuest());

        List<Double> premiumGuestsToAllocate = allocateGuests(potentialPremiumGuests, availablePremiumRooms);
        List<Double> economyGuestsToAllocate = allocateAndUpgradeEconomyGuests(potentialEconomyGuests, premiumGuestsToAllocate, availableEconomyRooms, availablePremiumRooms - premiumGuestsToAllocate.size());

        double revenueEconomy = calculateRevenue(economyGuestsToAllocate);
        double revenuePremium = calculateRevenue(premiumGuestsToAllocate);

        return new BookingResponse(premiumGuestsToAllocate.size(), revenuePremium, economyGuestsToAllocate.size(), revenueEconomy);
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

    private List<Double> allocateGuests(List<Double> potentialGuests, int availableRooms) {
        return potentialGuests.stream()
                .limit(availableRooms)
                .collect(Collectors.toList());
    }

    private List<Double> allocateAndUpgradeEconomyGuests(List<Double> potentialEconomyGuests, List<Double> premiumGuestsToAllocate, int availableEconomyRooms, int remainingPremiumRooms) {

        if (availableEconomyRooms >= potentialEconomyGuests.size())
            return potentialEconomyGuests;

        if (remainingPremiumRooms <= 0)
            return allocateGuests(potentialEconomyGuests, availableEconomyRooms);

        int numberOfEconomyGuestsToUpgrade = Math.min(potentialEconomyGuests.size() - availableEconomyRooms, remainingPremiumRooms);

        return allocateAndUpgrade(potentialEconomyGuests, premiumGuestsToAllocate, availableEconomyRooms, numberOfEconomyGuestsToUpgrade);
    }

    private List<Double> allocateAndUpgrade(List<Double> potentialEconomyGuests, List<Double> premiumGuestsToAllocate, int availableEconomyRooms, int numberOfEconomyGuestsToUpgrade) {
        List<Double> economyGuestsToAllocate = new ArrayList<>();
        for (int i = numberOfEconomyGuestsToUpgrade; i < numberOfEconomyGuestsToUpgrade + availableEconomyRooms; i++) {
            economyGuestsToAllocate.add(potentialEconomyGuests.get(i));
        }
        for (int i = 0; i < numberOfEconomyGuestsToUpgrade; i++) {
            premiumGuestsToAllocate.add(potentialEconomyGuests.get(i));
        }
        return economyGuestsToAllocate;
    }

    private double calculateRevenue(List<Double> guests) {
        return guests.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}