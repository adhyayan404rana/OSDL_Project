package com.hotel.controller;

import com.hotel.model.*;
import com.hotel.util.FileIOUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * RoomManagementController — CRUD operations on Room inventory.
 *
 * Demonstrates:
 * - COLLECTIONS: ArrayList for storage, HashMap for quick lookup
 * - SORTING: Collections.sort() to order rooms by price
 * - POLYMORPHISM: creates StandardRoom, ExecutiveRoom, FamilyRoom, or
 * LuxuryRoom based on RoomType
 * - WRAPPER CLASSES: Integer and Double used throughout
 * - EVENT HANDLING: button actions with Alert dialogs
 * - SYNCHRONIZATION: synchronized addRoom method
 */
public class RoomManagementController {

    // Form fields
    @FXML
    private TextField txtRoomNumber;
    @FXML
    private ComboBox<RoomType> cmbRoomType;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtMaxOccupancy;
    @FXML
    private TextField txtCustomAmenity;

    // Amenity Checkboxes
    @FXML
    private CheckBox chkPool;
    @FXML
    private CheckBox chkGym;
    @FXML
    private CheckBox chkSpa;
    @FXML
    private CheckBox chkMinibar;
    @FXML
    private CheckBox chkBalcony;
    @FXML
    private CheckBox chkRoomService;
    @FXML
    private CheckBox chkParking;
    @FXML
    private CheckBox chkBreakfast;

    // Table
    @FXML
    private TableView<Room> tblRooms;
    @FXML
    private TableColumn<Room, Integer> colRoomNumber;
    @FXML
    private TableColumn<Room, RoomType> colRoomType;
    @FXML
    private TableColumn<Room, Double> colPrice;
    @FXML
    private TableColumn<Room, Boolean> colAvailable;
    @FXML
    private TableColumn<Room, Integer> colOccupancy;
    @FXML
    private TableColumn<Room, String> colAmenities;

    // Detail panel
    @FXML
    private ImageView imgRoomPreview;
    @FXML
    private Label lblDetailType;
    @FXML
    private Label lblDetailDescription;
    @FXML
    private Label lblDetailPrice;
    @FXML
    private Label lblDetailOccupancy;
    @FXML
    private Label lblDetailAmenities;
    @FXML
    private Label lblDetailStatus;
    @FXML
    private Label lblDefaultAmenities;

    // Search
    @FXML
    private TextField txtSearch;

    /** HashMap for O(1) room lookup by room number */
    private HashMap<Integer, Room> roomMap = new HashMap<>();

    private ObservableList<Room> observableRooms;

    @FXML
    public void initialize() {
        // Populate ComboBox with RoomType enum values
        cmbRoomType.setItems(FXCollections.observableArrayList(RoomType.values()));

        // Auto-fill price and occupancy when room type changes
        cmbRoomType.setOnAction(e -> {
            RoomType selected = cmbRoomType.getValue();
            if (selected != null) {
                txtPrice.setText(String.valueOf(selected.getBaseTariff()));
                txtMaxOccupancy.setText(String.valueOf(selected.getMaxOccupancy()));

                // Update default amenities label
                lblDefaultAmenities.setText("Default: " + String.join(", ", selected.getDefaultAmenities()));
            }
        });

        // Load existing rooms from the shared in-memory store
        observableRooms = FXCollections.observableArrayList(DashboardController.roomStore);
        tblRooms.setItems(observableRooms);

        // Rebuild the HashMap from the shared store
        for (Room r : DashboardController.roomStore) {
            roomMap.put(r.getRoomNumber(), r);
        }

        // Table row click to show room details
        tblRooms.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                showRoomDetail(newSel);
            } else {
                resetDetailPanel();
            }
        });

        // Search filter
        if (txtSearch != null) {
            txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
                filterRooms(newVal);
            });
        }

        FileIOUtility.logEvent("Room Management screen opened.");
    }

    private void filterRooms(String query) {
        if (query == null || query.trim().isEmpty()) {
            observableRooms.setAll(DashboardController.roomStore);
            return;
        }
        String lower = query.toLowerCase().trim();
        List<Room> filtered = DashboardController.roomStore.stream()
                .filter(r -> String.valueOf(r.getRoomNumber()).contains(lower)
                        || r.getRoomType().name().toLowerCase().contains(lower)
                        || r.getAmenities().toLowerCase().contains(lower))
                .toList();
        observableRooms.setAll(filtered);
    }

    /**
     * Shows room detail in the right panel when a room is clicked.
     */
    private void showRoomDetail(Room room) {
        loadRoomImage(room.getImagePath());
        lblDetailType.setText(room.getRoomType().name());
        lblDetailDescription.setText(room.getRoomType().getDescription());
        lblDetailPrice.setText("₹ " + String.format("%,.2f", room.getPricePerNight()));
        lblDetailOccupancy.setText("Max Occupancy: " + room.getMaxOccupancy() + " guests");
        lblDetailAmenities.setText(room.getAmenities());
        lblDetailStatus.setText(room.getStatus() == com.hotel.model.RoomStatus.AVAILABLE ? "✅ Available"
                : "🔴 " + room.getStatus().getDisplayName());
        lblDetailStatus.setStyle(room.getStatus() == com.hotel.model.RoomStatus.AVAILABLE
                ? "-fx-text-fill: #00e676; -fx-font-size: 14; -fx-font-weight: bold;"
                : "-fx-text-fill: #ff5252; -fx-font-size: 14; -fx-font-weight: bold;");
    }

    /**
     * Resets the detail panel to its default state.
     */
    private void resetDetailPanel() {
        if (imgRoomPreview != null)
            imgRoomPreview.setImage(null);
        if (lblDetailType != null)
            lblDetailType.setText("—");
        if (lblDetailDescription != null)
            lblDetailDescription.setText("Select a room to view details");
        if (lblDetailPrice != null)
            lblDetailPrice.setText("₹ —");
        if (lblDetailOccupancy != null)
            lblDetailOccupancy.setText("");
        if (lblDetailAmenities != null)
            lblDetailAmenities.setText("");
        if (lblDetailStatus != null)
            lblDetailStatus.setText("");
    }

    /**
     * Loads a room image from resources. Shows a colored placeholder if not found.
     */
    private void loadRoomImage(String imagePath) {
        if (imgRoomPreview == null)
            return;
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is != null) {
                imgRoomPreview.setImage(new Image(is, 320, 200, true, true));
            } else {
                // Generate a simple placeholder
                imgRoomPreview.setImage(createPlaceholderImage());
            }
        } catch (Exception e) {
            imgRoomPreview.setImage(createPlaceholderImage());
        }
    }

    private Image createPlaceholderImage() {
        // Return null — the ImageView will just be empty, or we can set a style
        return null;
    }

    /**
     * Adds a new room — SYNCHRONIZED to prevent concurrent booking conflicts
     * Uses POLYMORPHISM to instantiate the correct room subclass
     */
    @FXML
    public void handleAddRoom() {
        // Input Validation
        String roomNumText = txtRoomNumber.getText().trim();
        RoomType selectedType = cmbRoomType.getValue();
        String priceText = txtPrice.getText().trim();

        if (roomNumText.isEmpty() || selectedType == null || priceText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please fill in all fields: Room Number, Type, and Price.");
            return;
        }

        Integer roomNumber; // Wrapper class
        Double price;

        try {
            roomNumber = Integer.valueOf(roomNumText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Room Number must be a valid integer.");
            return;
        }

        try {
            price = Double.valueOf(priceText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Price must be a valid number.");
            return;
        }

        if (roomNumber <= 0 || price <= 0) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Room Number and Price must be positive.");
            return;
        }

        // Check for duplicates via HashMap
        if (roomMap.containsKey(roomNumber)) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Room",
                    "Room #" + roomNumber + " already exists.");
            return;
        }

        // Create Room (Polymorphism)
        Room newRoom;
        switch (selectedType) {
            case STANDARD:
                newRoom = new StandardRoom(roomNumber, price);
                break;
            case EXECUTIVE:
                newRoom = new ExecutiveRoom(roomNumber, price);
                break;
            case FAMILY:
                newRoom = new FamilyRoom(roomNumber, price);
                break;
            default:
                // DELUXE, SUITE, PENTHOUSE, VILLA are all LuxuryRoom
                newRoom = new LuxuryRoom(roomNumber, selectedType, price);
                break;
        }

        // Set max occupancy if custom
        String occText = txtMaxOccupancy.getText().trim();
        if (!occText.isEmpty()) {
            try {
                newRoom.setMaxOccupancy(Integer.valueOf(occText));
            } catch (NumberFormatException ignored) {
            }
        }

        // Add custom amenities from checkboxes
        if (chkPool != null && chkPool.isSelected())
            newRoom.addCustomAmenity("Pool Access");
        if (chkGym != null && chkGym.isSelected())
            newRoom.addCustomAmenity("Gym Access");
        if (chkSpa != null && chkSpa.isSelected())
            newRoom.addCustomAmenity("Spa Access");
        if (chkMinibar != null && chkMinibar.isSelected())
            newRoom.addCustomAmenity("Minibar");
        if (chkBalcony != null && chkBalcony.isSelected())
            newRoom.addCustomAmenity("Private Balcony");
        if (chkRoomService != null && chkRoomService.isSelected())
            newRoom.addCustomAmenity("24/7 Room Service");
        if (chkParking != null && chkParking.isSelected())
            newRoom.addCustomAmenity("Dedicated Parking");
        if (chkBreakfast != null && chkBreakfast.isSelected())
            newRoom.addCustomAmenity("Complimentary Breakfast");

        // Add free-text custom amenity
        if (txtCustomAmenity != null) {
            String custom = txtCustomAmenity.getText().trim();
            if (!custom.isEmpty()) {
                // Support comma-separated custom amenities
                for (String a : custom.split(",")) {
                    newRoom.addCustomAmenity(a.trim());
                }
            }
        }

        // Synchronized add
        addRoomSynchronized(newRoom);

        // Update the UI
        observableRooms.add(newRoom);
        clearForm();

        showAlert(Alert.AlertType.INFORMATION, "Success",
                "Room #" + roomNumber + " (" + selectedType + ") added successfully!\n"
                        + "Amenities: " + newRoom.getAmenities());
        FileIOUtility.logEvent("Room #" + roomNumber + " added — " + selectedType + " @ ₹" + price);
    }

    /**
     * Synchronized method — ensures thread-safe room additions
     */
    private synchronized void addRoomSynchronized(Room room) {
        DashboardController.roomStore.add(room);
        roomMap.put(room.getRoomNumber(), room);
    }

    /**
     * Deletes the selected room from the table and in-memory store.
     */
    @FXML
    public void handleDeleteRoom() {
        Room selected = tblRooms.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a room to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete Room #" + selected.getRoomNumber() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Deletion");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                DashboardController.roomStore.remove(selected);
                roomMap.remove(selected.getRoomNumber());
                observableRooms.remove(selected);
                FileIOUtility.logEvent("Room #" + selected.getRoomNumber() + " deleted.");
                showAlert(Alert.AlertType.INFORMATION, "Deleted",
                        "Room #" + selected.getRoomNumber() + " removed.");
            }
        });
    }

    /**
     * Sorts rooms by price using Collections.sort()
     */
    @FXML
    public void handleSortByPrice() {
        Collections.sort(DashboardController.roomStore,
                Comparator.comparingDouble(Room::getPricePerNight));

        observableRooms.setAll(DashboardController.roomStore);
        FileIOUtility.logEvent("Rooms sorted by price.");
    }

    /**
     * Sorts rooms by room type name.
     */
    @FXML
    public void handleSortByType() {
        Collections.sort(DashboardController.roomStore,
                Comparator.comparing(r -> r.getRoomType().name()));

        observableRooms.setAll(DashboardController.roomStore);
        FileIOUtility.logEvent("Rooms sorted by type.");
    }

    /**
     * Adds a free-text custom amenity to the custom amenity text field
     */
    @FXML
    public void handleAddCustomAmenity() {
        if (txtCustomAmenity != null && !txtCustomAmenity.getText().trim().isEmpty()) {
            // Visual feedback
            showAlert(Alert.AlertType.INFORMATION, "Custom Amenity",
                    "Custom amenity '" + txtCustomAmenity.getText().trim() + "' will be added when you save the room.");
        }
    }

    /**
     * Navigates back to the Dashboard.
     */
    @FXML
    public void handleBackToDashboard() {
        navigateTo("/com/hotel/fxml/Dashboard.fxml", "Dashboard");
    }

    // Helpers
    private void clearForm() {
        txtRoomNumber.clear();
        cmbRoomType.setValue(null);
        txtPrice.clear();
        if (txtMaxOccupancy != null)
            txtMaxOccupancy.clear();
        if (txtCustomAmenity != null)
            txtCustomAmenity.clear();
        if (lblDefaultAmenities != null)
            lblDefaultAmenities.setText("");

        // Clear checkboxes
        if (chkPool != null)
            chkPool.setSelected(false);
        if (chkGym != null)
            chkGym.setSelected(false);
        if (chkSpa != null)
            chkSpa.setSelected(false);
        if (chkMinibar != null)
            chkMinibar.setSelected(false);
        if (chkBalcony != null)
            chkBalcony.setSelected(false);
        if (chkRoomService != null)
            chkRoomService.setSelected(false);
        if (chkParking != null)
            chkParking.setSelected(false);
        if (chkBreakfast != null)
            chkBreakfast.setSelected(false);

        // Also reset detail panel
        resetDetailPanel();
    }

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
            Stage stage = (Stage) tblRooms.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 750));
            stage.setTitle("Hotel Management — " + title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
