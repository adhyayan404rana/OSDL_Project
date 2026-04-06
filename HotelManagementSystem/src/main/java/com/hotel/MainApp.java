package com.hotel;

import com.hotel.controller.DashboardController;
import com.hotel.util.FileIOUtility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel/fxml/Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 750);

        primaryStage.setTitle("Hotel Management — Login");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    /**
     * Called automatically when the application window is closed.
     * Saves all Rooms, Customers, and Reservations permanently to file.
     */
    @Override
    public void stop() {
        FileIOUtility.saveDatabase(
                DashboardController.roomStore,
                DashboardController.guestStore,
                DashboardController.reservationStore,
                DashboardController.totalRevenue
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
