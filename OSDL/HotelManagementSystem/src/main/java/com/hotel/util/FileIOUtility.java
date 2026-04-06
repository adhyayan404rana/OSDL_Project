package com.hotel.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * FileIOUtility — demonstrates I/O STREAMS, SERIALIZATION, and RANDOMACCESSFILE
 * 1. generateInvoice() → writes a text-based receipt using FileWriter (I/O
 * Streams)
 * 2. serializeConfig() → saves config Map to a .dat file using
 * ObjectOutputStream (Serialization)
 * 3. deserializeConfig() → restores config Map from a .dat file using
 * ObjectInputStream
 * 4. logEvent() → appends log entries using RandomAccessFile (binary append)
 * 5. readRecentEvents() → reads the most recent N log entries
 */
public class FileIOUtility {

    private static final String INVOICE_DIR = "invoices/";
    private static final String CONFIG_FILE = "app_config.dat";
    private static final String LOG_FILE = "system_events.log";

    // TEXT INVOICE — FileWriter
    /**
     * Generates an itemized text invoice for a customer checkout.
     *
     * @param customerId     the customer's ID
     * @param customerName   the customer's name
     * @param roomNumber     the room number
     * @param daysStayed     total nights
     * @param tariffPerNight nightly rate
     * @param serviceCharge  additional service charge
     * @param totalAmount    calculated grand total
     * @return the file path of the generated invoice
     */
    public static String generateInvoice(Integer customerId, String customerName,
            Integer roomNumber, long daysStayed,
            Double tariffPerNight, Double serviceCharge,
            Double totalAmount, String paymentMode) {
        // Ensure the invoices directory exists
        File dir = new File(INVOICE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = INVOICE_DIR + "invoice_" + customerId + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            writer.write("╔══════════════════════════════════════════════╗\n");
            writer.write("║      HOTEL MANAGEMENT SYSTEM — INVOICE      ║\n");
            writer.write("╠══════════════════════════════════════════════╣\n");
            writer.write("║  Date       : " + padRight(timestamp, 31) + "║\n");
            writer.write("╠══════════════════════════════════════════════╣\n");
            writer.write("║  Customer ID: " + padRight(String.valueOf(customerId), 31) + "║\n");
            writer.write("║  Name       : " + padRight(customerName, 31) + "║\n");
            writer.write("║  Room No    : " + padRight(String.valueOf(roomNumber), 31) + "║\n");
            writer.write("╠══════════════════════════════════════════════╣\n");
            writer.write("║  Days Stayed     : " + padRight(String.valueOf(daysStayed), 26) + "║\n");
            writer.write("║  Tariff/Night    : ₹" + padRight(String.format("%.2f", tariffPerNight), 25) + "║\n");
            writer.write("║  Subtotal        : ₹" + padRight(String.format("%.2f", tariffPerNight * daysStayed), 25)
                    + "║\n");
            writer.write("║  Service Charge  : ₹" + padRight(String.format("%.2f", serviceCharge), 25) + "║\n");
            writer.write("║  Payment Mode    : " + padRight(paymentMode, 26) + "║\n");
            writer.write("╠══════════════════════════════════════════════╣\n");
            writer.write("║  GRAND TOTAL     : ₹" + padRight(String.format("%.2f", totalAmount), 25) + "║\n");
            writer.write("╚══════════════════════════════════════════════╝\n");
            writer.write("\nThank you for staying with us!\n");

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Log the event
        logEvent("Invoice generated for Customer #" + customerId + " — ₹" + String.format("%.2f", totalAmount));
        return fileName;
    }

    // SERIALIZATION — ObjectOutputStream / ObjectInputStream
    /**
     * Serializes a configuration map (e.g., tax rates) to a .dat binary file.
     */
    public static void serializeConfig(Map<String, Double> config) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(config);
            logEvent("Configuration serialized to " + CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializes the configuration map from the .dat file.
     * Returns a default config if the file doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Double> deserializeConfig() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            // Return default configuration
            Map<String, Double> defaults = new HashMap<>();
            defaults.put("SERVICE_CHARGE_RATE", 0.10); // 10%
            defaults.put("TAX_RATE", 0.12); // 12% GST
            return defaults;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONFIG_FILE))) {
            return (Map<String, Double>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // RANDOMACCESSFILE — binary log append
    /**
     * Appends a timestamped log entry to the binary system_events.log.
     */
    public static void logEvent(String message) {
        try (RandomAccessFile raf = new RandomAccessFile(LOG_FILE, "rw")) {
            raf.seek(raf.length()); // move to end of file
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String entry = "[" + timestamp + "] " + message + "\n";
            raf.writeBytes(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // READ RECENT EVENTS — for dashboard activity feed
    /**
     * Reads the most recent N log lines from the system_events.log file.
     * Returns them in reverse order (most recent first).
     */
    public static List<String> readRecentEvents(int count) {
        List<String> allLines = new ArrayList<>();
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            return allLines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    allLines.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return last N entries in reverse order
        List<String> recent = new ArrayList<>();
        int start = Math.max(0, allLines.size() - count);
        for (int i = allLines.size() - 1; i >= start; i--) {
            recent.add(allLines.get(i));
        }
        return recent;
    }

    // PERMANENT DATABASE STORAGE — Serialization (Rubric Requirement)
    private static final String DATABASE_FILE = "hotel_database.dat";

    /**
     * Saves all Rooms, Customers, Reservations, and Revenue permanently
     * to a binary .dat file using ObjectOutputStream (Serialization).
     * Called automatically when the application closes.
     */
    public static void saveDatabase(java.util.ArrayList<com.hotel.model.Room> rooms,
                                     java.util.ArrayList<com.hotel.model.Customer> guests,
                                     java.util.ArrayList<com.hotel.model.Reservation> reservations,
                                     Double totalRevenue) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            oos.writeObject(rooms);
            oos.writeObject(guests);
            oos.writeObject(reservations);
            oos.writeObject(totalRevenue);
            logEvent("Database saved permanently to " + DATABASE_FILE
                    + " (" + rooms.size() + " rooms, " + guests.size() + " guests, "
                    + reservations.size() + " reservations)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all Rooms, Customers, Reservations, and Revenue back from
     * the binary .dat file using ObjectInputStream (Deserialization).
     * Called automatically when the application starts.
     *
     * @return an Object array: [ArrayList<Room>, ArrayList<Customer>, ArrayList<Reservation>, Double]
     *         or null if no saved data exists.
     */
    @SuppressWarnings("unchecked")
    public static Object[] loadDatabase() {
        File file = new File(DATABASE_FILE);
        if (!file.exists()) {
            return null; // No saved data yet — first launch
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
            java.util.ArrayList<com.hotel.model.Room> rooms =
                    (java.util.ArrayList<com.hotel.model.Room>) ois.readObject();
            java.util.ArrayList<com.hotel.model.Customer> guests =
                    (java.util.ArrayList<com.hotel.model.Customer>) ois.readObject();
            java.util.ArrayList<com.hotel.model.Reservation> reservations =
                    (java.util.ArrayList<com.hotel.model.Reservation>) ois.readObject();
            Double totalRevenue = (Double) ois.readObject();

            logEvent("Database loaded from " + DATABASE_FILE
                    + " (" + rooms.size() + " rooms, " + guests.size() + " guests, "
                    + reservations.size() + " reservations)");

            return new Object[]{rooms, guests, reservations, totalRevenue};
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper
    private static String padRight(String s, int width) {
        if (s.length() >= width)
            return s.substring(0, width);
        return String.format("%-" + width + "s", s);
    }
}
