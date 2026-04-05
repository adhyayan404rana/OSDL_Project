package com.hotel.controller;

import com.hotel.model.Customer;
import com.hotel.model.Room;
import com.hotel.util.ClockTask;
import com.hotel.util.FileIOUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DashboardController — manages the main Dashboard screen.
 *
 * Demonstrates:
 *   - MULTITHREADING: starts a daemon ClockTask thread for live clock (Weeks 3 & 4)
 *   - EVENT HANDLING: button clicks navigate to Room Management / Billing / Guest Management (Week 9)
 *   - COLLECTIONS: ArrayList for room and guest storage (Week 8)
 */
public class DashboardController {

    @FXML private Label lblClock;
    @FXML private Label lblTotalRooms;
    @FXML private Label lblAvailableRooms;
    @FXML private Label lblActiveGuests;
    @FXML private Label lblRevenue;
    @FXML private Label lblRoomTypes;
    @FXML private ListView<String> lstRecentActivity;

    private ClockTask clockTask;
    private Thread clockThread;

    /**
     * Shared in-memory room store — ArrayList (Week 8: Collections).
     * This static list acts as the single source of truth while DB is not connected.
     */
    public static ArrayList<Room> roomStore = new ArrayList<>();

    /**
     * Shared in-memory guest store — ArrayList (Week 8: Collections).
     */
    public static ArrayList<Customer> guestStore = new ArrayList<>();

    /**
     * Shared in-memory reservation store.
     */
    public static ArrayList<com.hotel.model.Reservation> reservationStore = new ArrayList<>();

    /**
     * Tracks total revenue generated from invoices.
     */
    public static Double totalRevenue = 0.0;

    /**
     * Called automatically by FXMLLoader after .fxml is loaded.
     */
    @FXML
    public void initialize() {
        // Start the live clock on a daemon background thread (Multithreading — Week 3)
        clockTask = new ClockTask(lblClock);
        clockThread = new Thread(clockTask);
        clockThread.setDaemon(true); // dies with the application
        clockThread.setName("LiveClock-Thread");
        clockThread.start();

        // Log dashboard open event (RandomAccessFile — Week 5)
        FileIOUtility.logEvent("Dashboard opened.");

        // Refresh stats
        refreshStats();

        // Load recent activity
        loadRecentActivity();
    }

    /**
     * Refreshes summary statistics from the in-memory store.
     */
    @FXML
    public void handleRefresh() {
        refreshStats();
        loadRecentActivity();
    }

    private void refreshStats() {
        int total = roomStore.size();
        long available = roomStore.stream().filter(r -> r.getStatus() == com.hotel.model.RoomStatus.AVAILABLE).count();

        lblTotalRooms.setText(String.valueOf(total));
        lblAvailableRooms.setText(String.valueOf(available));
        lblActiveGuests.setText(String.valueOf(guestStore.size()));
        lblRevenue.setText("₹ " + String.format("%,.2f", totalRevenue));

        // Count distinct room types
        long distinctTypes = roomStore.stream().map(Room::getRoomType).distinct().count();
        lblRoomTypes.setText(String.valueOf(distinctTypes));
    }

    private void loadRecentActivity() {
        List<String> events = FileIOUtility.readRecentEvents(8);
        if (lstRecentActivity != null) {
            lstRecentActivity.getItems().clear();
            if (events.isEmpty()) {
                lstRecentActivity.getItems().add("No recent activity.");
            } else {
                lstRecentActivity.getItems().addAll(events);
            }
        }
    }

    /**
     * Navigates to Room Management screen.
     */
    @FXML
    public void handleManageRooms() {
        navigateTo("/com/hotel/fxml/RoomManagement.fxml", "Room Management");
    }

    /**
     * Navigates to Billing & Checkout screen.
     */
    @FXML
    public void handleBilling() {
        navigateTo("/com/hotel/fxml/Billing.fxml", "Billing & Checkout");
    }

    /**
     * Navigates to Guest Management screen.
     */
    @FXML
    public void handleGuestManagement() {
        navigateTo("/com/hotel/fxml/GuestManagement.fxml", "Guest Management");
    }

    /**
     * Helper to switch scenes by loading a new FXML.
     */
    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) lblClock.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 750));
            stage.setTitle("Hotel Management — " + title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
