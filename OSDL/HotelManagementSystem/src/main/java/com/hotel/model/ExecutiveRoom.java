package com.hotel.model;

/**
 * ExecutiveRoom — Concrete subclass for business-class accommodations (Week 1).
 * Demonstrates POLYMORPHISM: provides business-oriented amenity descriptions.
 * Extends abstract Room and implements the Amenities interface.
 */
public class ExecutiveRoom extends Room implements Amenities {

    private static final long serialVersionUID = 1L;

    public ExecutiveRoom(Integer roomNumber) {
        super(roomNumber, RoomType.EXECUTIVE, RoomType.EXECUTIVE.getBaseTariff());
    }

    public ExecutiveRoom(Integer roomNumber, Double customPrice) {
        super(roomNumber, RoomType.EXECUTIVE, customPrice);
    }

    // ─── Amenities Interface Implementation ──────────────────
    @Override
    public String provideWifi() {
        return "High-Speed Business Wi-Fi (50 Mbps)";
    }

    @Override
    public String provideBreakfast() {
        return "Business Breakfast + Lounge Access";
    }

    @Override
    public String provideLaundry() {
        return "Same-day express laundry & ironing";
    }

    @Override
    public String provideParking() {
        return "Reserved covered parking";
    }

    @Override
    public String providePool() {
        return "Pool access (all hours)";
    }

    @Override
    public String provideMinibar() {
        return "Complimentary coffee & tea minibar";
    }

    // ─── Abstract Method Implementation (Polymorphism) ───────
    @Override
    public String getDefaultAmenityDescription() {
        return provideWifi() + ", " + provideBreakfast() + ", " + provideLaundry()
                + ", " + provideParking() + ", " + provideMinibar();
    }
}
