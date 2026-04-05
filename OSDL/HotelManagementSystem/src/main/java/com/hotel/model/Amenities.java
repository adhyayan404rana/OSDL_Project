package com.hotel.model;

/**
 * Amenities Interface — demonstrates ABSTRACTION (Week 1).
 * Derived room classes must provide concrete implementations for all amenity categories.
 */
public interface Amenities {

    String provideWifi();

    String provideBreakfast();

    String provideLaundry();

    String provideParking();

    String providePool();

    String provideMinibar();
}
