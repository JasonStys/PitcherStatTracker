package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap;
import java.time.*;
import java.util.concurrent.ThreadLocalRandom;

/* This represents a single "Game" or "Period of training", usually 1-2 hours.
 *
 */
public class Session implements Comparable<Session> {
    // Identity Metadata
    private int sessionId;
    private String gameId;  // NULL for training
    private String pitcherId;
    private LocalDate timestamp;

    private final List<Pitch> pitches = new ArrayList<>();

    // Box score tallies non-bip (ball in play)
    private int outsRecorded = 0;
    private int strikeouts = 0;
    private int walks = 0;
    private int hitByPitch = 0;
    private int battersFaced = 0;
    private int nextID = 0;
    public int getNextID() {
        return nextID++;
    }

    // Allows sessions to be compared based on their timestamp.
    @Override
    public int compareTo(Session o) {
        return timestamp.compareTo(o.timestamp);
    }

    // bip buckets: truth for hits by type
    private enum BIPResult {OUT, SINGLE, DOUBLE, TRIPLE, HOME_RUN, REACHED_ERROR}

    private final EnumMap<BIPResult, Integer> bipCounts = new EnumMap<>(BIPResult.class);

    public Session() {
        // Empty constructor for when we're making a new session
        timestamp = LocalDate.now();
        for (BIPResult r : BIPResult.values()) bipCounts.put(r, 0);
    }

    public Session(int sessionId, String gameId, String pitcherId) {
        this.sessionId = sessionId;
        this.gameId = gameId;
        this.pitcherId = pitcherId;
        for (BIPResult r : BIPResult.values()) bipCounts.put(r, 0);
    }
    public Session(int sessionId, String pitcherId) {
        this(sessionId, null, pitcherId);
    }

    public void addPitch(Pitch p) {
        // Give each added pitch an incremental ID. These may differ from the ID's used in SessionEditPanel, but will still be unique.
        if (p.getID() == -1) {
            p.setID(getNextID());
        }
        pitches.add(p);


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
                //reset count
            }
            case ReachOnError -> {
                incrementBip(BIPResult.REACHED_ERROR);
                battersFaced++;
                //reset count
            }
            case Walk -> {
                walks++;
                battersFaced++;
                //reset count
            }
            case Strikeout -> {
                strikeouts++;
                outsRecorded++;
                battersFaced++;
                //reset count
            }
            case HitByPitch -> {
                hitByPitch++;
                battersFaced++;
                //reset count
            }
            case Strike, Ball, Foul -> {
                //Nothing for now gonna be important for calculating the count
                //ex. if there has been 3 balls and 2 strikes its a 3-2 count

            }
        }
    }
    /*
    public void addPitch(Pitch.Type type, Pitch.Result result, boolean inZone, double speed) {
        Pitch p = new Pitch(type, result, inZone, speed);
        // If result==Hit and no bases provided, assume 1 (single)
        if (result == Pitch.Result.Hit) {
            p.setBases(1);
        }
        addPitch(p);
        System.out.println("Added: " + p);
    }
     */
    private void incrementBip(BIPResult r) {
        bipCounts.put(r, bipCounts.get(r) + 1);
    }
    public void showPitches() {
        System.out.println("Session " + sessionId + " Pitch Log:");
        for (Pitch p : pitches) {
            System.out.println(p);
        }
    }

    public void cloneMetaData(Session old) {
        this.gameId = old.gameId;
        this.pitcherId = old.pitcherId;
        this.timestamp = old.timestamp;
        this.sessionId = old.sessionId;
    }

    // getters
    public List<Pitch> getPitches() {return pitches;}
    public LocalDate getTimestamp() {return timestamp;}
    public void setTimestamp(LocalDate date) {this.timestamp = date;}

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

    public String inningsPitched() {
        int outs = outsRecorded;
        int full = outs / 3;
        int rem = outs % 3;
        return full + "." + rem;
    }

    private static LocalDate randomDateLastFiveYears() {
        int currentYear = LocalDate.now().getYear();

        // Pick a random year in [currentYear - 4, currentYear]
        int year = ThreadLocalRandom.current().nextInt(currentYear - 4, currentYear + 1);

        // Rough MLB regular season window: April 1 – October 1
        LocalDate seasonStart = LocalDate.of(year, 4, 1);
        LocalDate seasonEnd   = LocalDate.of(year, 10, 1);

        long startEpochDay = seasonStart.toEpochDay();
        long endEpochDay   = seasonEnd.toEpochDay();

        // Random day in [startEpochDay, endEpochDay]
        long randomEpochDay = ThreadLocalRandom.current()
                .nextLong(startEpochDay, endEpochDay + 1);

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    public static Session createRandomSessionData(int numPitches) {
        Session exampleSession = new Session();
        exampleSession.timestamp = randomDateLastFiveYears();
        for (int i = 0; i < numPitches; i++) {
            exampleSession.addPitch(Pitch.randomPitch());
        }
        return exampleSession;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp.toString());
        sb.append(" : " + totalPitches() + " pitches, ");

        return sb.toString();
    }
}
