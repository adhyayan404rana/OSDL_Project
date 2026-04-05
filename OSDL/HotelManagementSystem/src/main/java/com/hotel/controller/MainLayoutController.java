package com.hotel.controller;

import com.hotel.service.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Main Application Layout (Enterprise Sidebar Structure).
 */
public class MainLayoutController {

    @FXML private BorderPane mainContainer;
    @FXML private Label lblUserRole;

    @FXML
    public void initialize() {
        // Set the logged-in user label
        if (AuthService.getCurrentUser() != null) {
            lblUserRole.setText("👤 " + AuthService.getCurrentUser().toString());
        }

        // Load the Dashboard by default in the center view
        loadView("/com/hotel/fxml/Dashboard.fxml");
    }

    @FXML
    public void navToDashboard() {
        loadView("/com/hotel/fxml/Dashboard.fxml");
    }

    @FXML
    public void navToRooms() {
        loadView("/com/hotel/fxml/RoomManagement.fxml");
    }

    @FXML
    public void navToReservations() {
        loadView("/com/hotel/fxml/Reservations.fxml");
    }

    @FXML
    public void navToGuests() {
        loadView("/com/hotel/fxml/GuestManagement.fxml");
    }

    @FXML
    public void navToBilling() {
        loadView("/com/hotel/fxml/Billing.fxml");
    }

    @FXML
    public void navToReports() {
        loadView("/com/hotel/fxml/Reporting.fxml");
    }

    @FXML
    public void navToHousekeeping() {
        loadView("/com/hotel/fxml/Housekeeping.fxml");
    }

    @FXML
    public void handleLogout() {
        AuthService.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 750));
            stage.setTitle("Hotel Management System — Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }

    /**
     * Swaps the center node of the BorderPane with the newly requested FXML view.
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            mainContainer.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
