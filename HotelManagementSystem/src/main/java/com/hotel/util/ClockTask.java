package com.hotel.util;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ClockTask — demonstrates MULTITHREADING using the Runnable interface
 *
 * Runs on a background daemon thread and updates a JavaFX Label every second
 * with the current date and time using Platform.runLater() for thread safety.
 */
public class ClockTask implements Runnable {

    private final Label clockLabel;// JavaFX text box that will display time on screen
    private volatile boolean running = true;// is a switch to turn the thread on or off
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy  |  HH:mm:ss");

    public ClockTask(Label clockLabel) {
        this.clockLabel = clockLabel;
    }

    @Override
    public void run() {
        while (running) {
            String currentTime = LocalDateTime.now().format(FORMATTER);

            // Update JavaFX UI from the FX Application Thread
            Platform.runLater(() -> clockLabel.setText(currentTime));

            try {
                Thread.sleep(1000); // update every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Gracefully stops the clock thread.
     */
    public void stop() {
        running = false;
    }
}
