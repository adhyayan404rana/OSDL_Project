package com.hotel.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Reservation entity containing booking constraints.
 */
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer reservationId;
    private String guestName;
    private String guestPhone;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // e.g. "CONFIRMED", "CANCELLED"

    public Reservation(Integer reservationId, String guestName, String guestPhone, Room room, LocalDate startDate, LocalDate endDate) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "CONFIRMED";
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Property Getter mappings for FXML TableView
    public Integer getRoomNumber() {
        return room != null ? room.getRoomNumber() : null;
    }

    public String getRoomType() {
        return room != null ? room.getRoomType().name() : "";
    }

    @Override
    public String toString() {
        return "Res#" + reservationId + " (" + guestName + ") -> Room " + getRoomNumber() + " [" + startDate + " to " + endDate + "]";
    }
}
