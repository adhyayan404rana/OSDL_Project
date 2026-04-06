package com.hotel.model;

/**
 * Defines the roles available in the system for role-based authentication.
 */
public enum UserRole {
    ADMIN("Administrator"),
    MANAGER("Hotel Manager"),
    RECEPTIONIST("Receptionist");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
