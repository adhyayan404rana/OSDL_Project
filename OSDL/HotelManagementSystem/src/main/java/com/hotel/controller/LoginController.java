package com.hotel.controller;

import com.hotel.service.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Login Screen.
 */
public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    @FXML
    public void initialize() {
        // Platform.runLater can be used to set focus if needed.
    }

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (AuthService.authenticate(username, password)) {
            // Login successful! Transition to Main Navigation Layout
            navigateToMain();
        } else {
            // Show error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Authentication Failed");
            alert.setContentText("Invalid username or password.\n\nTry: admin / admin123");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }

    private void navigateToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/fxml/MainLayout.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 750));
            stage.setTitle("Hotel Management System — Enterprise Edition");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
