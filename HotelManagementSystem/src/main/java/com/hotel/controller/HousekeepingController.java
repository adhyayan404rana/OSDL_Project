package com.hotel.controller;

import com.hotel.model.Room;
import com.hotel.model.RoomStatus;
import com.hotel.util.FileIOUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.util.List;

/**
 * Controller for the Housekeeping Module.
 */
public class HousekeepingController {

    @FXML private TableView<Room> tblHousekeeping;
    @FXML private TableColumn<Room, Integer> colRoomNumber;
    @FXML private TableColumn<Room, String> colRoomType;
    @FXML private TableColumn<Room, RoomStatus> colStatus;

    private ObservableList<Room> observableRooms;

    @FXML
    public void initialize() {
        // Filter rooms to only show those that are OCCUPIED or MAINTENANCE
        List<Room> filteredRooms = DashboardController.roomStore.stream()
                .filter(r -> r.getStatus() == RoomStatus.OCCUPIED || r.getStatus() == RoomStatus.MAINTENANCE)
                .toList();

        observableRooms = FXCollections.observableArrayList(filteredRooms);
        tblHousekeeping.setItems(observableRooms);
        
        FileIOUtility.logEvent("Housekeeping Module opened.");
    }

    @FXML
    public void handleMarkMaintenance() {
        Room selected = tblHousekeeping.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a room to update.");
            return;
        }

        if (selected.getStatus() == RoomStatus.MAINTENANCE) {
            showAlert(Alert.AlertType.INFORMATION, "Already in Maintenance", "This room is already under maintenance.");
            return;
        }

        selected.setStatus(RoomStatus.MAINTENANCE);
        refreshTable();
        FileIOUtility.logEvent("Room #" + selected.getRoomNumber() + " marked for Maintenance by " + (com.hotel.service.AuthService.getCurrentUser() != null ? com.hotel.service.AuthService.getCurrentUser().getUsername() : "System"));
    }

    @FXML
    public void handleMarkClean() {
        Room selected = tblHousekeeping.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a room to update.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Mark Room #" + selected.getRoomNumber() + " as clean and AVAILABLE?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                selected.setStatus(RoomStatus.AVAILABLE);
                observableRooms.remove(selected); // Removing it from this view as it's no longer needing attention
                FileIOUtility.logEvent("Room #" + selected.getRoomNumber() + " marked AVAILABLE by " + (com.hotel.service.AuthService.getCurrentUser() != null ? com.hotel.service.AuthService.getCurrentUser().getUsername() : "System"));
            }
        });
    }

    private void refreshTable() {
        tblHousekeeping.refresh();
    }

    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
