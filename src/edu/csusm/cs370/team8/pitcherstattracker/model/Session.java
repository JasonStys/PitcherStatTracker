package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.ArrayList;
import java.util.List;

/* This represents a single "Game" or "Period of training", usually 1-2 hours.
 * THIS CLASS IS WHAT HAS ALL THE STAT FIELDS.
 */
public class Session {
    // Needs a Timestamp, but I need to look at what's the best java module to use for that.
    // ArrayList<Pitch> pitch; // All the different games and trainings this pitcher has done.
    private List<Pitch> pitches;  // a list to store multiple Pitch objects
    private int sessionId;

    public Session(int sessionId) {
        this.sessionId = sessionId;
        this.pitches = new ArrayList<>();
    }

    public void addPitch(Pitch.Type type, Pitch.Result result, boolean inZone, double speed) {
        Pitch newPitch = new Pitch(type, result, inZone, speed);
        pitches.add(newPitch);
        System.out.println("Added: " + newPitch);
    }

    public void showPitches() {
        System.out.println("Session " + sessionId + " Pitch Log:");
        for (Pitch p : pitches) {
            System.out.println(p);
        }
    }
}

