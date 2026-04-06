package com.hotel.controller;

import com.hotel.model.Customer;
import com.hotel.util.FileIOUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * GuestManagementController — manages the Guest Management screen.
 *
 * Demonstrates:
 *   - COLLECTIONS: ArrayList/ObservableList for guest data (Week 8)
 *   - ENCAPSULATION: uses Customer model with getters/setters (Week 1)
 *   - EVENT HANDLING: search, filter, and table interaction (Week 9)
 */
public class GuestManagementController {

    @FXML private TableView<Customer> tblGuests;
    @FXML private TableColumn<Customer, Integer> colGuestId;
    @FXML private TableColumn<Customer, String> colGuestName;
    @FXML private TableColumn<Customer, String> colGuestPhone;
    @FXML private TableColumn<Customer, Integer> colGuestRoom;
    @FXML private TableColumn<Customer, String> colCheckIn;
    @FXML private TableColumn<Customer, String> colCheckOut;

    @FXML private TextField txtSearchGuest;
    @FXML private Label lblTotalGuests;
    @FXML private Label lblActiveGuests;
    @FXML private Label lblCheckedOut;

    private ObservableList<Customer> observableGuests;

    @FXML
    public void initialize() {
        observableGuests = FXCollections.observableArrayList(DashboardController.guestStore);
        tblGuests.setItems(observableGuests);

        // Search filter
        if (txtSearchGuest != null) {
            txtSearchGuest.textProperty().addListener((obs, oldVal, newVal) -> {
                filterGuests(newVal);
            });
        }

        refreshStats();
        FileIOUtility.logEvent("Guest Management screen opened.");
    }

    private void filterGuests(String query) {
        if (query == null || query.trim().isEmpty()) {
            observableGuests.setAll(DashboardController.guestStore);
            refreshStats();
            return;
        }
        String lower = query.toLowerCase().trim();
        List<Customer> filtered = DashboardController.guestStore.stream()
                .filter(c -> c.getName().toLowerCase().contains(lower)
                        || String.valueOf(c.getCustomerId()).contains(lower)
                        || String.valueOf(c.getAssignedRoomNumber()).contains(lower)
                        || (c.getPhone() != null && c.getPhone().contains(lower)))
                .toList();
        observableGuests.setAll(filtered);
        refreshStats();
    }

    private void refreshStats() {
        int total = DashboardController.guestStore.size();
        // Active guests = those in rooms currently occupied
        long active = DashboardController.guestStore.stream()
                .filter(c -> {
                    if (c.getAssignedRoomNumber() == null) return false;
                    return DashboardController.roomStore.stream()
                            .anyMatch(r -> r.getRoomNumber().equals(c.getAssignedRoomNumber())
                                    && r.getStatus() == com.hotel.model.RoomStatus.OCCUPIED);
                })
                .count();

        if (lblTotalGuests != null) lblTotalGuests.setText(String.valueOf(total));
        if (lblActiveGuests != null) lblActiveGuests.setText(String.valueOf(active));
        if (lblCheckedOut != null) lblCheckedOut.setText(String.valueOf(total - active));
    }

    /**
     * Deletes the selected guest record.
     */
    @FXML
    public void handleDeleteGuest() {
        Customer selected = tblGuests.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a guest to remove.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Remove guest record for " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Removal");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                DashboardController.guestStore.remove(selected);
                observableGuests.remove(selected);
                refreshStats();
                FileIOUtility.logEvent("Guest #" + selected.getCustomerId() + " (" + selected.getName() + ") removed.");
                showAlert(Alert.AlertType.INFORMATION, "Removed",
                        "Guest record for " + selected.getName() + " removed.");
            }
        });
    }

    /**
     * Refreshes the guest list.
     */
    @FXML
    public void handleRefresh() {
        observableGuests.setAll(DashboardController.guestStore);
        refreshStats();
    }

    /**
     * Navigates back to the Dashboard.
     */
    @FXML
    public void handleBackToDashboard() {
        navigateTo("/com/hotel/fxml/Dashboard.fxml", "Dashboard");
    }

    // ─── Helpers ──────────────────────────────────────────────
    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) tblGuests.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 750));
            stage.setTitle("Hotel Management — " + title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
