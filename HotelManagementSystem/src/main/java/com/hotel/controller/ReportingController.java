package com.hotel.controller;

import com.hotel.model.Customer;
import com.hotel.util.FileIOUtility;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the Reporting Module.
 */
public class ReportingController {

    @FXML private ComboBox<String> cmbReportType;
    @FXML private TextArea txtReportPreview;

    @FXML
    public void initialize() {
        cmbReportType.setItems(FXCollections.observableArrayList(
                "Revenue Summary",
                "Guest History",
                "Room Occupancy Status"
        ));
        cmbReportType.setValue("Revenue Summary");

        FileIOUtility.logEvent("Reporting engine opened.");
        generatePreview(); // initial generation
    }

    @FXML
    public void handleGenerateReport() {
        generatePreview();
    }

    private void generatePreview() {
        String reportType = cmbReportType.getValue();
        StringBuilder sb = new StringBuilder();

        sb.append("========================================================\n");
        sb.append("                  ").append(reportType.toUpperCase()).append("\n");
        sb.append("         Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n");
        sb.append("========================================================\n\n");

        if ("Revenue Summary".equals(reportType)) {
            sb.append("Total Revenue Collected (This Session): ₹").append(String.format("%,.2f", DashboardController.totalRevenue)).append("\n\n");
            sb.append("Historical logs note: Check system_events.log for full historical audit trace of income.\n");
        } else if ("Guest History".equals(reportType)) {
            sb.append("Total Registered Guests: ").append(DashboardController.guestStore.size()).append("\n\n");
            for (Customer c : DashboardController.guestStore) {
                sb.append(String.format("ID: %-6s | Name: %-20s | Room: %-5s | In: %s | Out: %s\n",
                        c.getCustomerId(), c.getName(), c.getAssignedRoomNumber(), c.getCheckInDate(), c.getCheckOutDate()));
            }
        } else if ("Room Occupancy Status".equals(reportType)) {
            long total = DashboardController.roomStore.size();
            long countAvail = DashboardController.roomStore.stream().filter(r -> r.getStatus() == com.hotel.model.RoomStatus.AVAILABLE).count();
            sb.append("Occupancy Rate: ").append(String.format("%.1f", ((total - countAvail) / (double) total) * 100)).append("%\n\n");
            
            sb.append("AVAILABLE (").append(countAvail).append(" rooms):\n");
            DashboardController.roomStore.stream().filter(r -> r.getStatus() == com.hotel.model.RoomStatus.AVAILABLE)
                    .forEach(r -> sb.append("  - Room ").append(r.getRoomNumber()).append(" (").append(r.getRoomType().name()).append(")\n"));
            
            sb.append("\nOCCUPIED/UNAVAILABLE (").append(total - countAvail).append(" rooms):\n");
            DashboardController.roomStore.stream().filter(r -> r.getStatus() != com.hotel.model.RoomStatus.AVAILABLE)
                    .forEach(r -> sb.append("  - Room ").append(r.getRoomNumber()).append(" (").append(r.getStatus().getDisplayName()).append(")\n"));
        }

        txtReportPreview.setText(sb.toString());
    }

    @FXML
    public void handleExportCSV() {
        String reportType = cmbReportType.getValue();
        File dir = new File("exports");
        if (!dir.exists()) dir.mkdirs();

        String fileName = "exports/" + reportType.replace(" ", "_") + "_" + System.currentTimeMillis() + ".csv";
        
        try (FileWriter writer = new FileWriter(fileName)) {
            if ("Revenue Summary".equals(reportType)) {
                writer.write("Metric,Value\n");
                writer.write("Session Revenue," + DashboardController.totalRevenue + "\n");
            } else if ("Guest History".equals(reportType)) {
                writer.write("ID,Name,Phone,Room,CheckIn,CheckOut\n");
                for (Customer c : DashboardController.guestStore) {
                    writer.write(c.getCustomerId() + "," + c.getName() + "," + c.getPhone() + "," + c.getAssignedRoomNumber() + "," + c.getCheckInDate() + "," + c.getCheckOutDate() + "\n");
                }
            } else if ("Room Occupancy Status".equals(reportType)) {
                writer.write("RoomNum,Type,Status,Price\n");
                for (com.hotel.model.Room r : DashboardController.roomStore) {
                    writer.write(r.getRoomNumber() + "," + r.getRoomType() + "," + r.getStatus().getDisplayName() + "," + r.getPricePerNight() + "\n");
                }
            }
            
            FileIOUtility.logEvent("Exported '" + reportType + "' to " + fileName);
            showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Report saved to:\n" + new File(fileName).getAbsolutePath());
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Could not write the CSV file.");
        }
    }

    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
