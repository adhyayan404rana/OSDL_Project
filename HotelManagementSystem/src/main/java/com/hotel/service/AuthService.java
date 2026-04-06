package com.hotel.service;

import com.hotel.model.User;
import com.hotel.model.UserRole;
import com.hotel.util.FileIOUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Service Layer for handling Authentication.
 * Simulates a DB connection by hardcoding a few roles and users.
 */
public class AuthService {

    // Simulated user database: Map<Username, Password>
    private static final Map<String, String> userDb = new HashMap<>();
    
    // Map<Username, Role>
    private static final Map<String, UserRole> roleDb = new HashMap<>();

    // The globally tracked logged-in user.
    private static User currentUser = null;

    static {
        // Hardcode mock data for enterprise demo without actual DB.
        // In a real system, passwords would be hashed.
        userDb.put("admin", "admin123");
        roleDb.put("admin", UserRole.ADMIN);

        userDb.put("manager", "manager123");
        roleDb.put("manager", UserRole.MANAGER);

        userDb.put("recep", "recep123");
        roleDb.put("recep", UserRole.RECEPTIONIST);
    }

    /**
     * Attempts to simulate a login check.
     */
    public static boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        
        String storedPassword = userDb.get(username.toLowerCase());
        if (storedPassword != null && storedPassword.equals(password)) {
            UserRole role = roleDb.get(username.toLowerCase());
            currentUser = new User(username.toLowerCase(), role);
            FileIOUtility.logEvent("User logged in: " + currentUser.toString());
            return true;
        }
        return false;
    }

    /**
     * Returns the currently active session user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logs out the current user.
     */
    public static void logout() {
        if (currentUser != null) {
            FileIOUtility.logEvent("User logged out: " + currentUser.toString());
            currentUser = null;
        }
    }
}
