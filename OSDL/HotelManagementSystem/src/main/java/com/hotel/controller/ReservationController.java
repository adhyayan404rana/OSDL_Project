package com.hotel.controller;

import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.service.AuthService;
import com.hotel.util.FileIOUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller for Advanced Reservation System.
 */
public class ReservationController {

    // Form fields
    @FXML private TextField txtGuestName;
    @FXML private TextField txtPhone;
    @FXML private ComboBox<String> cmbRoom;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;

    // Table
    @FXML private TableView<Reservation> tblReservations;
    @FXML private TableColumn<Reservation, Integer> colResId;
    @FXML private TableColumn<Reservation, String> colGuestName;
    @FXML private TableColumn<Reservation, String> colPhone;
    @FXML private TableColumn<Reservation, Integer> colRoomNum;
    @FXML private TableColumn<Reservation, String> colRoomType;
    @FXML private TableColumn<Reservation, LocalDate> colStart;
    @FXML private TableColumn<Reservation, LocalDate> colEnd;
    @FXML private TableColumn<Reservation, String> colStatus;

    private ObservableList<Reservation> observableReservations;
    private int nextResId = 1000;

    @FXML
    public void initialize() {
        // Init table
        observableReservations = FXCollections.observableArrayList(DashboardController.reservationStore);
        tblReservations.setItems(observableReservations);

        // Calculate next ID
        if (!DashboardController.reservationStore.isEmpty()) {
            int maxId = DashboardController.reservationStore.stream()
                    .mapToInt(Reservation::getReservationId)
                    .max().orElse(1000);
            nextResId = maxId + 1;
        }

        // Init Room Dropdown
        updateRoomDropdown();

        FileIOUtility.logEvent("Reservations module opened.");
    }

    private void updateRoomDropdown() {
        if (cmbRoom != null) {
            cmbRoom.setItems(FXCollections.observableArrayList(
                    DashboardController.roomStore.stream()
                            .map(r -> "Room #" + r.getRoomNumber() + " — " + r.getRoomType().name())
                            .toList()
            ));
        }
    }

    @FXML
    public void handleBookReservation() {
        String name = txtGuestName.getText().trim();
        String phone = txtPhone.getText().trim();
        String roomSelection = cmbRoom.getValue();
        LocalDate start = dpStartDate.getValue();
        LocalDate end = dpEndDate.getValue();

        // Validation
        if (name.isEmpty() || phone.isEmpty() || roomSelection == null || start == null || end == null) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Form", "Please fill in all fields.");
            return;
        }
        if (start.isAfter(end) || start.isEqual(end)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Dates", "End date must be strictly after start date.");
            return;
        }
        if (start.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Invalid Dates", "Cannot book in the past.");
            return;
        }

        // Extract room number
        int roomNum = Integer.parseInt(roomSelection.replaceAll("[^0-9]", ""));
        Optional<Room> selectedRoomOpt = DashboardController.roomStore.stream()
                .filter(r -> r.getRoomNumber() == roomNum)
                .findFirst();

        if (selectedRoomOpt.isEmpty()) return;
        Room selectedRoom = selectedRoomOpt.get();

        // Check conflicts
        boolean conflict = DashboardController.reservationStore.stream()
                .filter(res -> res.getStatus().equals("CONFIRMED") && res.getRoom().getRoomNumber().equals(selectedRoom.getRoomNumber()))
                .anyMatch(res -> isOverlap(start, end, res.getStartDate(), res.getEndDate()));

        if (conflict) {
            showAlert(Alert.AlertType.ERROR, "Booking Conflict", "This room is already reserved during the chosen dates.");
            return;
        }

        // Success! Create reservation
        Reservation newRes = new Reservation(nextResId++, name, phone, selectedRoom, start, end);
        DashboardController.reservationStore.add(newRes);
        observableReservations.add(newRes);
        
        // Also automatically block the room if the reservation is starting today
        if (start.equals(LocalDate.now())) {
            selectedRoom.setStatus(RoomStatus.RESERVED);
        }

        clearForm();
        String user = AuthService.getCurrentUser() != null ? AuthService.getCurrentUser().getUsername() : "System";
        FileIOUtility.logEvent("Reservation #" + newRes.getReservationId() + " created by " + user + ".");
        
        showAlert(Alert.AlertType.INFORMATION, "Booking Confirmed", "Reservation #" + newRes.getReservationId() + " successfully saved!");
    }

    @FXML
    public void handleCancelReservation() {
        Reservation selected = tblReservations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a reservation to cancel.");
            return;
        }

        if ("CANCELLED".equals(selected.getStatus())) {
            showAlert(Alert.AlertType.INFORMATION, "Already Cancelled", "This reservation is already cancelled.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Cancel Reservation #" + selected.getReservationId() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                selected.setStatus("CANCELLED");
                
                // If the room was currently RESERVED for this booking, release it
                if (selected.getStartDate().isEqual(LocalDate.now()) && selected.getRoom().getStatus() == RoomStatus.RESERVED) {
                    selected.getRoom().setStatus(RoomStatus.AVAILABLE);
                }

                tblReservations.refresh();
                String user = AuthService.getCurrentUser() != null ? AuthService.getCurrentUser().getUsername() : "System";
                FileIOUtility.logEvent("Reservation #" + selected.getReservationId() + " cancelled by " + user + ".");
            }
        });
    }

    private boolean isOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        // Overlap condition: start1 < end2 AND end1 > start2
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private void clearForm() {
        txtGuestName.clear();
        txtPhone.clear();
        cmbRoom.setValue(null);
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
