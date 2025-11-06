package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap;

/* This represents a single "Game" or "Period of training", usually 1-2 hours.
 * THIS CLASS IS WHAT HAS ALL THE STAT FIELDS.
 */
public class Session {
    // Identity
    private int sessionId;
    private String gameId;  // NULL for training
    private String pitcherId;

    private List<Pitch> pitches =  new ArrayList<Pitch>();

    // Box score tallies non-bip (ball in play)
    private int outsRecorded = 0;
    private int strikeouts = 0;
    private int walks = 0;
    private int hitByPitch = 0;
    private int battersFaced = 0;

    // bip buckets: truth for hits by type
    private enum BIPResult {OUT, SINGLE, DOUBLE, TRIPLE, HOME_RUN, REACHED_ERROR}
    private final EnumMap<BIPResult, Integer> bipCounts = new EnumMap<>(BIPResult.class);

    public Session(int sessionId, String gameId, String pitcherId) {
        this.sessionId = sessionId;
        this.gameId = gameId;
        this.pitcherId = pitcherId;
        for (BIPResult r : BIPResult.values()) bipCounts.put(r, 0);
    }

    public void addPitch(Pitch p) {
        pitches.add(p);
        // Derive hit buckets if this pitch produced a hit
        if (p.getResult() == Pitch.Result.Hit) {
            int bases = p.getBases(); // expected 1,2,3,4
            switch (bases) {
                case 1 -> incrementBip(BIPResult.SINGLE);
                case 2 -> incrementBip(BIPResult.DOUBLE);
                case 3 -> incrementBip(BIPResult.TRIPLE);
                case 4 -> incrementBip(BIPResult.HOME_RUN);
                default -> incrementBip(BIPResult.SINGLE); // if bases not set or invalid
            }
            battersFaced++; // end of PA (Plate Appearance)
        }
        // For CalledStrike/Ball/Foul we do nothing; not terminal by themselves
        // until count logic is added to Pitch.
    }
    public void addPitch(Pitch.Type type, Pitch.Result result, boolean inZone, double speed) {
        Pitch p = new Pitch(type, result, inZone, speed);
        // If result==Hit and no bases provided, assume 1 (single)
        if (result == Pitch.Result.Hit) {
            p.setBases(1);
        }
        addPitch(p);
        System.out.println("Added: " + p);
    }
    public void showPitches() {
        System.out.println("Session " + sessionId + " Pitch Log:");
        for (Pitch p : pitches) {
            System.out.println(p);
        }
    }

    // getters
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
    private void incrementBip(BIPResult r) { bipCounts.put(r, bipCounts.get(r) + 1); }
}
