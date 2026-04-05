package com.hotel.model;

import java.util.Arrays;
import java.util.List;

/**
 * RoomType Enum — demonstrates ENUMERATIONS with constructor (Week 2).
 * Each constant is initialized with base tariff, description, max occupancy,
 * default amenities list, and image path using wrapper classes.
 */
public enum RoomType {

    STANDARD(2000.0, "Comfortable room with essential amenities", 2,
            Arrays.asList("Basic Wi-Fi", "Daily Housekeeping", "AC"),
            "/com/hotel/images/rooms/standard.jpg"),

    DELUXE(4500.0, "Spacious room with city view and premium furnishing", 2,
            Arrays.asList("High-Speed Wi-Fi", "City View", "Premium Bedding", "Mini Fridge", "Daily Housekeeping"),
            "/com/hotel/images/rooms/deluxe.jpg"),

    EXECUTIVE(6000.0, "Business-class room with dedicated work space", 2,
            Arrays.asList("High-Speed Wi-Fi", "Work Desk", "Complimentary Coffee", "Ironing Station", "Meeting Room Access"),
            "/com/hotel/images/rooms/executive.jpg"),

    FAMILY(5500.0, "Large room designed for families with extra bedding", 4,
            Arrays.asList("Wi-Fi", "Extra Beds", "Kids Amenity Kit", "Childproofing", "Board Games"),
            "/com/hotel/images/rooms/family.jpg"),

    SUITE(8000.0, "Luxury suite with separate living area", 3,
            Arrays.asList("High-Speed Wi-Fi", "Living Room", "Premium Minibar", "Bathrobe & Slippers", "Room Service 24x7"),
            "/com/hotel/images/rooms/suite.jpg"),

    PENTHOUSE(15000.0, "Top-floor penthouse with panoramic views and private terrace", 4,
            Arrays.asList("Fibre Wi-Fi", "Private Terrace", "Panoramic View", "Personal Butler", "Jacuzzi", "Premium Minibar"),
            "/com/hotel/images/rooms/penthouse.jpg"),

    VILLA(20000.0, "Private villa with garden, pool, and full kitchen", 6,
            Arrays.asList("Fibre Wi-Fi", "Private Pool", "Garden", "Full Kitchen", "Personal Chef", "Spa Access", "Airport Transfer"),
            "/com/hotel/images/rooms/villa.jpg");

    private final Double baseTariff;            // Wrapper class usage (Week 2)
    private final String description;
    private final Integer maxOccupancy;          // Wrapper class usage (Week 2)
    private final List<String> defaultAmenities;
    private final String imagePath;

    RoomType(Double baseTariff, String description, Integer maxOccupancy,
             List<String> defaultAmenities, String imagePath) {
        this.baseTariff = baseTariff;
        this.description = description;
        this.maxOccupancy = maxOccupancy;
        this.defaultAmenities = defaultAmenities;
        this.imagePath = imagePath;
    }

    public Double getBaseTariff() {
        return baseTariff;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    public List<String> getDefaultAmenities() {
        return defaultAmenities;
    }

    public String getImagePath() {
        return imagePath;
    }
}
