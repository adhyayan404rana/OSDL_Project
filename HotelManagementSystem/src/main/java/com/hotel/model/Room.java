package com.hotel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Room — Abstract base class demonstrating ENCAPSULATION and INHERITANCE
 *
 * - Private fields with public getters/setters (encapsulation)
 * - Abstract method getAmenities() forces concrete subclasses to implement (inheritance)
 * - Implements Serializable for potential future serialization needs
 * - Now includes customAmenities, imagePath, and maxOccupancy
 */
public abstract class Room implements Serializable {

    private static final long serialVersionUID = 2L;

    private Integer roomNumber;             // Wrapper class 
    private RoomType roomType;
    private Double pricePerNight;
    private RoomStatus status;
    private Integer maxOccupancy;
    private String imagePath;
    private ArrayList<String> customAmenities;  // Collections

    // ─── Constructor ─────────────────────────────────────────
    public Room(Integer roomNumber, RoomType roomType, Double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.status = RoomStatus.AVAILABLE;
        this.maxOccupancy = roomType.getMaxOccupancy();
        this.imagePath = roomType.getImagePath();
        this.customAmenities = new ArrayList<>();
    }

    // ─── Abstract Method ─────────────────────────────────────
    /**
     * Returns the default amenities specific to this room type.
     * Demonstrates abstraction — each subclass provides its own amenity list.
     */
    public abstract String getDefaultAmenityDescription();

    /**
     * Returns the full amenities string combining default + custom amenities.
     */
    public String getAmenities() {
        StringBuilder sb = new StringBuilder(getDefaultAmenityDescription());
        if (!customAmenities.isEmpty()) {
            sb.append(", ");
            sb.append(String.join(", ", customAmenities));
        }
        return sb.toString();
    }

    /**
     * Returns just the default amenities from the RoomType enum.
     */
    public List<String> getDefaultAmenitiesList() {
        return roomType.getDefaultAmenities();
    }

    // ─── Getters & Setters (Encapsulation) ───────────────────
    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<String> getCustomAmenities() {
        return customAmenities;
    }

    public void setCustomAmenities(ArrayList<String> customAmenities) {
        this.customAmenities = customAmenities;
    }

    public void addCustomAmenity(String amenity) {
        if (amenity != null && !amenity.trim().isEmpty() && !customAmenities.contains(amenity.trim())) {
            customAmenities.add(amenity.trim());
        }
    }

    public void removeCustomAmenity(String amenity) {
        customAmenities.remove(amenity);
    }

    @Override
    public String toString() {
        return "Room #" + roomNumber + " [" + roomType + "] ₹" + pricePerNight + "/night"
                + " (Max: " + maxOccupancy + " guests)"
                + " — " + status.getDisplayName();
    }
}
