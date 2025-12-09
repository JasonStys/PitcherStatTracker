package edu.csusm.cs370.team8.pitcherstattracker.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.csusm.cs370.team8.pitcherstattracker.model.Session;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

// This class is unused. Tyler wrote this when he was figuring our Jackson and testing saving sessions to .json files.
public class SessionDatabase {
    // Root "sessions" Directory
    private static final File SESSION_DIR = new File("sessions");

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    static {
        if (!SESSION_DIR.exists()) {
            SESSION_DIR.mkdirs();
        }
    }

    public static void save(Session session) throws IOException {
        // Makes sure that a pitcherId exists
        //if (session.getPitcherId() == null) {
        //    throw new IllegalArgumentException("Session has no Pitcher ID");
        //}

        // Make a pitcherId specific folder
        //File pitcherDir = new File(SESSION_DIR, session.getPitcherId());
        //if (!pitcherDir.exists()) {
        //    pitcherDir.mkdirs();
        //}

        String date = session.getTimestamp().toString().replace("-", "");
        File out = new File(SESSION_DIR, "session_" + session.getTimestamp() + "_" + session.hashCode() + ".json");
        mapper.writeValue(out, session);
    }

    public static Session load(File file) throws IOException {
        return mapper.readValue(file, Session.class);
    }
}
