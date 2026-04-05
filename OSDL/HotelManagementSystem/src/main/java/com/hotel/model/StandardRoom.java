package com.hotel.model;

/**
 * StandardRoom — Concrete subclass demonstrating POLYMORPHISM and INTERFACE
 * implementation (Week 1).
 * Extends abstract Room and implements the Amenities interface.
 * Provides basic-tier amenities for budget-conscious guests.
 */
public class StandardRoom extends Room implements Amenities {

    private static final long serialVersionUID = 2L;

    public StandardRoom(Integer roomNumber) {
        super(roomNumber, RoomType.STANDARD, RoomType.STANDARD.getBaseTariff());
    }

    public StandardRoom(Integer roomNumber, Double customPrice) {
        super(roomNumber, RoomType.STANDARD, customPrice);
    }

    // Amenities Interface Implementation
    @Override
    public String provideWifi() {
        return "Basic Wi-Fi (5 Mbps)";
    }

    @Override
    public String provideBreakfast() {
        return "Continental Breakfast included";
    }

    @Override
    public String provideLaundry() {
        return "Self-service laundry available";
    }

    @Override
    public String provideParking() {
        return "Shared parking (first-come basis)";
    }

    @Override
    public String providePool() {
        return "Pool access (limited hours)";
    }

    @Override
    public String provideMinibar() {
        return "No minibar";
    }

    // Abstract Method Implementation (Polymorphism)
    @Override
    public String getDefaultAmenityDescription() {
        return provideWifi() + ", " + provideBreakfast() + ", " + provideLaundry();
    }
}
