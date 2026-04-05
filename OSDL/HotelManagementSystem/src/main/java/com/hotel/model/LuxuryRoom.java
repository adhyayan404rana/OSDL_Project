package com.hotel.model;

/**
 * LuxuryRoom — Concrete subclass demonstrating POLYMORPHISM and INTERFACE
 * implementation
 * Provides premium amenities for DELUXE, SUITE, PENTHOUSE, and VILLA room
 * types.
 */
public class LuxuryRoom extends Room implements Amenities {

    private static final long serialVersionUID = 2L;

    public LuxuryRoom(Integer roomNumber, RoomType roomType) {
        super(roomNumber, roomType, roomType.getBaseTariff());
    }

    public LuxuryRoom(Integer roomNumber, RoomType roomType, Double customPrice) {
        super(roomNumber, roomType, customPrice);
    }

    // Amenities Interface Implementation
    @Override
    public String provideWifi() {
        return "High-Speed Wi-Fi (100 Mbps)";
    }

    @Override
    public String provideBreakfast() {
        return "Premium Buffet Breakfast with Room Service";
    }

    @Override
    public String provideLaundry() {
        return "Complimentary Dry-Cleaning & Laundry";
    }

    @Override
    public String provideParking() {
        return "Dedicated valet parking";
    }

    @Override
    public String providePool() {
        return "Unlimited pool & spa access";
    }

    @Override
    public String provideMinibar() {
        return "Fully-stocked premium minibar";
    }

    // Abstract Method Implementation (Polymorphism)
    @Override
    public String getDefaultAmenityDescription() {
        return provideWifi() + ", " + provideBreakfast() + ", " + provideLaundry()
                + ", " + provideParking() + ", " + providePool() + ", " + provideMinibar();
    }
}
