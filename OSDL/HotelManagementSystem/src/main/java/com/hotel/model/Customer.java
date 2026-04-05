package com.hotel.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Customer Model — demonstrates ENCAPSULATION (Week 1).
 * Private fields with public getters/setters. Uses Integer wrapper for ID.
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer customerId;           // Wrapper class (Week 2)
    private String name;
    private String phone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer assignedRoomNumber;   // Wrapper class (Week 2)

    // ─── Constructors ────────────────────────────────────────
    public Customer() {
    }

    public Customer(Integer customerId, String name, String phone,
                    LocalDate checkInDate, LocalDate checkOutDate,
                    Integer assignedRoomNumber) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.assignedRoomNumber = assignedRoomNumber;
    }

    // ─── Getters & Setters ───────────────────────────────────
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getAssignedRoomNumber() {
        return assignedRoomNumber;
    }

    public void setAssignedRoomNumber(Integer assignedRoomNumber) {
        this.assignedRoomNumber = assignedRoomNumber;
    }

    @Override
    public String toString() {
        return "Customer #" + customerId + " — " + name + " (Room " + assignedRoomNumber + ")";
    }
}
