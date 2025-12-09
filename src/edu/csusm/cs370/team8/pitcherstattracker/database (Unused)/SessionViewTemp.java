package edu.csusm.cs370.team8.pitcherstattracker.view;

import java.io.File;
import java.io.IOException;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// This class is unused. Tyler wrote this when testing Jackson and saving sessions as .json files.
public class SessionViewTemp {

    private final ObjectMapper objectMapper;

    public SessionViewTemp() {
        objectMapper = new ObjectMapper();
        // Register Java 8 Date/Time module so LocalDate works
        objectMapper.registerModule(new JavaTimeModule());
    }

    private final File sessionFolder = new File("sessions"); // folder containing session JSON files

    public void displayAllSessions() {
        if (!sessionFolder.exists() || !sessionFolder.isDirectory()) {
            System.err.println("Session folder not found: " + sessionFolder.getAbsolutePath());
            return;
        }

        File[] sessionFiles = sessionFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (sessionFiles == null || sessionFiles.length == 0) {
            System.out.println("No session files found.");
            return;
        }

        for (File file : sessionFiles) {
            try {
                Session session = objectMapper.readValue(file, Session.class);
                System.out.println("Loaded session from file: " + file.getName());
                //System.out.println("Pitcher ID: " + session.getPitcherId() + ", Timestamp: " + session.getTimestamp());
                session.showPitches();
                System.out.println("-------------------------------");
            } catch (IOException e) {
                System.err.println("Failed to load session from " + file.getName() + ": " + e.getMessage());
            }
        }
    }
}
