package com.hotel.model;

/**
 * Represents a system user for authentication.
 */
public class User {
    private String username;
    private UserRole role;

    public User(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public String toString() {
        return username + " (" + role.getDisplayName() + ")";
    }
}
