package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap;
import java.time.*;

/* This represents a single "Game" or "Period of training", usually 1-2 hours.
 * It represents a composition of Pitches
 */
public class Session implements Comparable<Session> {
    // Identity Metadata
    private int sessionId;
    private LocalDate timestamp;

    // These aren't used.
    //private String gameId;
    //private String pitcherId;

    private final List<Pitch> pitches = new ArrayList<>();

    // Box score tallies non-bip (ball in play).
    // Calculated when a new Pitch is added to our list.
    private int outsRecorded = 0;
    private int strikeouts = 0;
    private int walks = 0;
    private int hitByPitch = 0;
    private int battersFaced = 0;

    // These are for the IDs of PITCHES, not for SessionID, so it's tied to the object not static.
    private int nextPitchID = 0;
    public int getNextID() {
        // Catch if it reset to zero. It doesn't seem to do this anymore now that it's saving.
        if ((pitches.size() - 1) > nextPitchID) {
            nextPitchID = pitches.size() - 1;
        }
        return nextPitchID++;
    }

    // These are static and for SessionID.
    private static int nextSeshID = 0;
    public static int getNextSeshID() {
        return nextSeshID++; // Returns the ID value before incrementing it.
    }
    public static void setSessionID(int id) {
        if (id > nextSeshID) {  // Given value must be larger than what we have.
            nextSeshID = id; // Sets the value. Should ONLY be called when loading from json.
        }
    }

    // Allows sessions to be compared and sorted based on their timestamp.
    @Override
    public int compareTo(Session o) {
        return timestamp.compareTo(o.timestamp);
    }

    // bip buckets: Tallies for hits by type
    private enum BIPResult {OUT, SINGLE, DOUBLE, TRIPLE, HOME_RUN, REACHED_ERROR}
    private final EnumMap<BIPResult, Integer> bipCounts = new EnumMap<>(BIPResult.class);

    // Empty constructor for when we're making a new session
    public Session() {
        timestamp = LocalDate.now(); // Defaults to current date
        sessionId = getNextSeshID();
        for (BIPResult r : BIPResult.values()) bipCounts.put(r, 0); // Initialize with zeroes.
    }

    // Constructor with metadata for JSON loading.
    public Session(int id, LocalDate date, int pitchID) {
        this.sessionId = id;
        setSessionID(id); // Resets the static MAX session ID if we load something larger.
        this.timestamp = date;
        this.nextPitchID = pitchID;
        //this.gameId = gameId;
        //this.pitcherId = pitcherId;
        for (BIPResult r : BIPResult.values()) bipCounts.put(r, 0); // Initialize with zeroes.
    }

    public void addPitch(Pitch p) {
        // Give each added pitch an incremental ID. These may differ from the ID's used in SessionEditPanel, but will still be unique.
        if (p.getID() == -1) {
            p.setID(getNextID());
        }
        pitches.add(p);

        // Increase the tallies for each pitch that is added.
        switch (p.getResult()) {
            case Hit -> {
                switch (p.getBases()) {
                    case 1 -> incrementBip(BIPResult.SINGLE);
                    case 2 -> incrementBip(BIPResult.DOUBLE);
                    case 3 -> incrementBip(BIPResult.TRIPLE);
                    case 4 -> incrementBip(BIPResult.HOME_RUN);
                    default -> incrementBip(BIPResult.SINGLE); // if bases not set or invalid
                }
                battersFaced++; // end of PA (Plate Appearance)
            }
            case BallInPlayOut -> {
                incrementBip(BIPResult.OUT);
                outsRecorded++;
                battersFaced++;
            }
            case ReachOnError -> {
                incrementBip(BIPResult.REACHED_ERROR);
                battersFaced++;
            }
            case Walk -> {
                walks++;
                battersFaced++;
            }
            case Strikeout -> {
                strikeouts++;
                outsRecorded++;
                battersFaced++;
            }
            case HitByPitch -> {
                hitByPitch++;
                battersFaced++;
            }
            case Strike, Ball, Foul -> {
                // Aidan:
                //Nothing for now gonna be important for calculating the count
                //ex. if there has been 3 balls and 2 strikes its a 3-2 count

            }
        }
    }
    // increases a specific tally
    private void incrementBip(BIPResult r) {
        bipCounts.put(r, bipCounts.get(r) + 1);
    }

    // Clones the metadata from one session to another.
    public void cloneMetaData(Session old) {
        //this.gameId = old.gameId;
        //this.pitcherId = old.pitcherId;
        this.timestamp = old.timestamp;
        this.sessionId = old.sessionId;
        this.nextPitchID = old.nextPitchID;
    }

    // getters
    public List<Pitch> getPitches() {return pitches;}
    public LocalDate getTimestamp() {return timestamp;}
    public void setTimestamp(LocalDate date) {this.timestamp = date;}
    public int getSessionId() {return sessionId;}

    public int singles() { return bipCounts.get(BIPResult.SINGLE); }
    public int doubles() { return bipCounts.get(BIPResult.DOUBLE); }
    public int triples() { return bipCounts.get(BIPResult.TRIPLE); }
    public int homeRuns() { return bipCounts.get(BIPResult.HOME_RUN); }
    public int outsInPlay() { return bipCounts.get(BIPResult.OUT); }
    public int reachedOnError() { return bipCounts.get(BIPResult.REACHED_ERROR); }

    public int hits() { return singles() + doubles() + triples() + homeRuns(); }
    public int extraBaseHits() { return doubles() + triples() + homeRuns(); }
    public int totalBases() { return singles() + 2*doubles() + 3*triples() + 4*homeRuns(); }

    public int strikeouts() { return strikeouts; }
    public int walks() { return walks; }
    public int hitByPitch() { return hitByPitch; }
    public int battersFaced() { return battersFaced; }
    public int totalPitches() { return pitches.size(); }

    // Unused Debug printing to console
    public void showPitches() {
        System.out.println("Session " + sessionId + " Pitch Log:");
        for (Pitch p : pitches) {
            System.out.println(p);
        }
    }
    // Unused Debug printing to console
    public String inningsPitched() {
        int outs = outsRecorded;
        int full = outs / 3;
        int rem = outs % 3;
        return full + "." + rem;
    }

    // toString() is what JList prints in the panels.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp.toString());
        sb.append(" : " + totalPitches() + " pitches: [");
        sb.append(battersFaced() + " batters faced, ");
        sb.append(hits() + " hits, ");
        sb.append(outsRecorded + " outs, ");
        sb.append(walks() + " walks]");

        return sb.toString();
    }

    /* Unused field
    public String getPitcherId() {
        return pitcherId;
    }
     */
}
