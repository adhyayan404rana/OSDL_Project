/**
 * Module descriptor for the Hotel Management System.
 * Required by JavaFX 17+ for modular Java applications.
 */
module com.hotel {
    requires transitive javafx.controls;
    requires javafx.fxml;

    // Open packages to JavaFX for FXML reflection
    opens com.hotel to javafx.fxml;
    opens com.hotel.controller to javafx.fxml;
    opens com.hotel.model to javafx.base, javafx.fxml;

    // Export public API
    exports com.hotel;
    exports com.hotel.model;
    exports com.hotel.controller;
    exports com.hotel.util;
}
