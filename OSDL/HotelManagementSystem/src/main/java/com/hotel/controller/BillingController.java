package com.hotel.controller;

import com.hotel.model.Customer;
import com.hotel.model.Room;
import com.hotel.util.FileIOUtility;
import com.hotel.util.Pair;
import javafx.collections.FXCollections;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * BillingController — handles customer checkout and invoice generation.
 *
 * Demonstrates:
 *   - GENERICS: Pair<Integer, Integer> binds Customer ID → Room Number (Week 7)
 *   - WRAPPER CLASSES: Integer, Double used for financial calc (Week 2)
 *   - I/O STREAMS: generates text invoice via FileIOUtility (Week 5)
 *   - SERIALIZATION: loads tax/service config from .dat file (Week 6)
 *   - COLLECTIONS: ComboBox populated from ArrayList (Week 8)
 *   - EVENT HANDLING: button actions with validation alerts (Week 9)
 */
public class BillingController {

    // ─── FXML Fields ─────────────────────────────────────────
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtCustomerName;
    @FXML private TextField txtPhone;
    @FXML private ComboBox<String> cmbRoom;
    @FXML private DatePicker dpCheckIn;
    @FXML private DatePicker dpCheckOut;
    @FXML private TextField txtDiscountCode;

    // Bill Summary Labels
    @FXML private ComboBox<String> cmbPaymentMode;

    @FXML private Label lblRoomType;
    @FXML private Label lblTariff;
    @FXML private Label lblNights;
    @FXML private Label lblSubtotal;
    @FXML private Label lblServiceCharge;
    @FXML private Label lblTax;
    @FXML private Label lblDiscount;
    @FXML private Label lblGrandTotal;
    @FXML private Label lblStatus;
    @FXML private Label lblRoomAmenities;

    // Room Image
    @FXML private ImageView imgRoomBilling;

    // ─── State ───────────────────────────────────────────────
    private Room selectedRoom;
    private Double calculatedTotal = 0.0;  // Wrapper class (Week 2)
    private Double serviceChargeAmount = 0.0;
    private Double discountAmount = 0.0;
    private long daysStayed = 0;

    /** Supported discount codes (HashMap — Week 8) */
    private static final HashMap<String, Double> DISCOUNT_CODES = new HashMap<>();
    static {
        DISCOUNT_CODES.put("WELCOME10", 0.10);    // 10% off
        DISCOUNT_CODES.put("SUMMER20", 0.20);      // 20% off
        DISCOUNT_CODES.put("LOYALTY15", 0.15);     // 15% off
        DISCOUNT_CODES.put("FIRST25", 0.25);       // 25% off for first-timers
        DISCOUNT_CODES.put("WEEKEND5", 0.05);      // 5% off
    }

    /** Config loaded from serialized .dat file (Serialization — Week 6) */
    private Map<String, Double> config;

    @FXML
    public void initialize() {
        // Load configuration from serialized .dat file (Week 6)
        config = FileIOUtility.deserializeConfig();

        // Populate room ComboBox from shared in-memory store (Collections — Week 8)
        cmbRoom.setItems(FXCollections.observableArrayList(
                DashboardController.roomStore.stream()
                        .filter(r -> r.getStatus() == com.hotel.model.RoomStatus.AVAILABLE)
                        .map(r -> "Room #" + r.getRoomNumber() + " — " + r.getRoomType()

                                + " (₹" + String.format("%,.0f", r.getPricePerNight()) + "/night)")
                        .toList()
        ));

        // When a room is selected, store the reference and show its image
        cmbRoom.setOnAction(e -> {
            int idx = cmbRoom.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                // Get the available rooms in the same order
                var availableRooms = DashboardController.roomStore.stream()
                        .filter(r -> r.getStatus() == com.hotel.model.RoomStatus.AVAILABLE)
                        .toList();
                if (idx < availableRooms.size()) {
                    selectedRoom = availableRooms.get(idx);
                    loadRoomImage(selectedRoom.getImagePath());
                    if (lblRoomAmenities != null) {
                        lblRoomAmenities.setText(selectedRoom.getAmenities());
                    }
                }
            }
        });

        // Initialize payment modes
        if (cmbPaymentMode != null) {
            cmbPaymentMode.setItems(FXCollections.observableArrayList("Cash", "Credit Card", "UPI"));
            cmbPaymentMode.setValue("Cash");
        }

        FileIOUtility.logEvent("Billing screen opened.");
    }

    private void loadRoomImage(String imagePath) {
        if (imgRoomBilling == null) return;
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is != null) {
                imgRoomBilling.setImage(new Image(is, 280, 170, true, true));
            } else {
                imgRoomBilling.setImage(null);
            }
        } catch (Exception e) {
            imgRoomBilling.setImage(null);
        }
    }

    /**
     * Calculates the total bill with service charges, tax, and discount.
     * Uses Pair<Integer, Integer> to bind Customer ID → Room Number (Generics — Week 7).
     */
    @FXML
    public void handleCalculate() {
        // ── Validation ───────────────────────────────────────
        if (!validateInputs()) return;

        Integer customerId = Integer.valueOf(txtCustomerId.getText().trim()); // Wrapper (Week 2)
        LocalDate checkIn = dpCheckIn.getValue();
        LocalDate checkOut = dpCheckOut.getValue();

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Dates",
                    "Check-out date must be after check-in date.");
            return;
        }

        if (selectedRoom == null) {
            showAlert(Alert.AlertType.WARNING, "No Room Selected", "Please select a room.");
            return;
        }

        // ── Generic Pair to bind Customer → Room (Week 7) ───
        Pair<Integer, Integer> booking = new Pair<>(customerId, selectedRoom.getRoomNumber());
        System.out.println("Booking pair created: " + booking);

        // ── Billing Calculation ──────────────────────────────
        daysStayed = ChronoUnit.DAYS.between(checkIn, checkOut);
        Double tariffPerNight = selectedRoom.getPricePerNight(); // Wrapper (Week 2)
        Double subtotal = tariffPerNight * daysStayed;

        Double serviceRate = config.getOrDefault("SERVICE_CHARGE_RATE", 0.10);
        Double taxRate = config.getOrDefault("TAX_RATE", 0.12);

        serviceChargeAmount = subtotal * serviceRate;
        Double taxAmount = subtotal * taxRate;

        // Apply discount if code is valid
        discountAmount = 0.0;
        Double discountRate = 0.0;
        if (txtDiscountCode != null) {
            String code = txtDiscountCode.getText().trim().toUpperCase();
            if (!code.isEmpty() && DISCOUNT_CODES.containsKey(code)) {
                discountRate = DISCOUNT_CODES.get(code);
                discountAmount = subtotal * discountRate;
            } else if (!code.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Code",
                        "Discount code '" + code + "' is not valid.\n\nAvailable codes: "
                                + String.join(", ", DISCOUNT_CODES.keySet()));
            }
        }

        calculatedTotal = subtotal + serviceChargeAmount + taxAmount - discountAmount;

        // ── Update Bill Summary Labels ───────────────────────
        lblRoomType.setText(selectedRoom.getRoomType().toString());
        lblTariff.setText("₹ " + String.format("%,.2f", tariffPerNight));
        lblNights.setText(String.valueOf(daysStayed));
        lblSubtotal.setText("₹ " + String.format("%,.2f", subtotal));
        lblServiceCharge.setText("₹ " + String.format("%,.2f", serviceChargeAmount));
        lblTax.setText("₹ " + String.format("%,.2f", taxAmount));

        if (lblDiscount != null) {
            if (discountAmount > 0) {
                lblDiscount.setText("- ₹ " + String.format("%,.2f", discountAmount)
                        + " (" + (int)(discountRate * 100) + "% off)");
                lblDiscount.setStyle("-fx-text-fill: #00e676; -fx-font-size: 14; -fx-font-weight: bold;");
            } else {
                lblDiscount.setText("₹ 0.00");
                lblDiscount.setStyle("-fx-text-fill: #a0a0c0; -fx-font-size: 14;");
            }
        }

        lblGrandTotal.setText("₹ " + String.format("%,.2f", calculatedTotal));

        lblStatus.setText("✅ Bill calculated successfully. Click 'Generate Invoice' to save.");

        FileIOUtility.logEvent("Bill calculated for Customer #" + customerId
                + " — Room #" + selectedRoom.getRoomNumber() + " — Total: ₹" + String.format("%.2f", calculatedTotal));
    }

    /**
     * Generates a text invoice file, marks the room as occupied, and adds guest to store.
     * Uses FileIOUtility.generateInvoice() (I/O Streams — Week 5).
     */
    @FXML
    public void handleGenerateInvoice() {
        if (calculatedTotal <= 0) {
            showAlert(Alert.AlertType.WARNING, "No Calculation",
                    "Please calculate the bill first.");
            return;
        }

        Integer customerId = Integer.valueOf(txtCustomerId.getText().trim());
        String customerName = txtCustomerName.getText().trim();

        // Create Customer object
        Customer customer = new Customer(
                customerId, customerName, txtPhone.getText().trim(),
                dpCheckIn.getValue(), dpCheckOut.getValue(),
                selectedRoom.getRoomNumber()
        );

        // Add to guest store (Collections — Week 8)
        DashboardController.guestStore.add(customer);

        // Track revenue
        DashboardController.totalRevenue += calculatedTotal;

        String paymentMode = cmbPaymentMode != null && cmbPaymentMode.getValue() != null ? cmbPaymentMode.getValue() : "Cash";

        // Generate text invoice (I/O Streams — Week 5)
        String invoicePath = FileIOUtility.generateInvoice(
                customerId, customerName,
                selectedRoom.getRoomNumber(), daysStayed,
                selectedRoom.getPricePerNight(),
                serviceChargeAmount, calculatedTotal,
                paymentMode
        );

        // Mark room as occupied
        selectedRoom.setStatus(com.hotel.model.RoomStatus.OCCUPIED);

        // Serialize current config (Serialization — Week 6)
        FileIOUtility.serializeConfig(config);

        if (invoicePath != null) {
            lblStatus.setText("📄 Invoice saved to: " + invoicePath);
            showAlert(Alert.AlertType.INFORMATION, "Invoice Generated",
                    "Invoice saved successfully!\nPath: " + invoicePath
                            + "\n\nCustomer: " + customer.toString()
                            + "\nTotal: ₹" + String.format("%,.2f", calculatedTotal));
        } else {
            lblStatus.setText("❌ Failed to generate invoice.");
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate invoice file.");
        }
    }

    /**
     * Validates all input fields with clear error messages (Event Handling — Week 9).
     */
    private boolean validateInputs() {
        String idText = txtCustomerId.getText().trim();
        String name = txtCustomerName.getText().trim();
        String phone = txtPhone.getText().trim();

        if (idText.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields",
                    "Please fill in Customer ID, Name, and Phone.");
            return false;
        }

        try {
            Integer.valueOf(idText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid ID", "Customer ID must be a valid integer.");
            return false;
        }

        if (!phone.matches("\\d{10}")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Phone",
                    "Phone number must be exactly 10 digits.");
            return false;
        }

        if (dpCheckIn.getValue() == null || dpCheckOut.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Dates",
                    "Please select both Check-In and Check-Out dates.");
            return false;
        }

        if (cmbRoom.getSelectionModel().getSelectedIndex() < 0) {
            showAlert(Alert.AlertType.WARNING, "No Room", "Please select a room.");
            return false;
        }

        return true;
    }

    /**
     * Navigates back to Dashboard.
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
            Stage stage = (Stage) txtCustomerId.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 750));
            stage.setTitle("Hotel Management — " + title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
