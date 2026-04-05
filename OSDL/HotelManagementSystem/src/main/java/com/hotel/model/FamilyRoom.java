package com.hotel.model;

/**
 * FamilyRoom — Concrete subclass for family-oriented accommodations (Week 1).
 * Demonstrates POLYMORPHISM: provides family-oriented amenity descriptions.
 * Extends abstract Room and implements the Amenities interface.
 */
public class FamilyRoom extends Room implements Amenities {

    private static final long serialVersionUID = 1L;

    public FamilyRoom(Integer roomNumber) {
        super(roomNumber, RoomType.FAMILY, RoomType.FAMILY.getBaseTariff());
    }

    public FamilyRoom(Integer roomNumber, Double customPrice) {
        super(roomNumber, RoomType.FAMILY, customPrice);
    }

    // ─── Amenities Interface Implementation ──────────────────
    @Override
    public String provideWifi() {
        return "Family Wi-Fi (25 Mbps)";
    }

    @Override
    public String provideBreakfast() {
        return "Family Buffet Breakfast (kids eat free)";
    }

    @Override
    public String provideLaundry() {
        return "Family laundry service available";
    }

    @Override
    public String provideParking() {
        return "Family parking (SUV-friendly)";
    }

    @Override
    public String providePool() {
        return "Kids pool & main pool access";
    }

    @Override
    public String provideMinibar() {
        return "Snack bar (kid-friendly options)";
    }

    // ─── Abstract Method Implementation (Polymorphism) ───────
    @Override
    public String getDefaultAmenityDescription() {
        return provideWifi() + ", " + provideBreakfast() + ", " + provideLaundry()
                + ", " + providePool() + ", " + provideMinibar();
    }
}
