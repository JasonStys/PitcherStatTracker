package edu.csusm.cs370.team8.pitcherstattracker.controller;

import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;
import edu.csusm.cs370.team8.pitcherstattracker.model.Session;

import java.time.*;
import java.util.*;

/*
Core pitching stats engine.

- Aggregates raw stats from Sessions.
- Produces PitchingStats objects with both raw counts and derived metrics.
- Supports grouping: career total, by year, or by month.

No Swing / view dependencies. Safe to use for tables, charts, exports, etc.
*/
public class StatCalculator {

    /*
     How to group sessions into buckets.
     */
    public enum Grouping {
        CAREER,   // one bucket: all sessions combined
        BY_YEAR,  // one bucket per calendar year (key: "2024", "2025", ...)
        BY_MONTH,  // one bucket per YearMonth (key: "2024-03", "2025-11", ...)
        BY_DAY
    }

    /*
     Aggregated pitching stats for one bucket
     (e.g. career, one year, or one month).

     Contains raw counts plus helper methods for derived metrics
     like AVG, OBP, SLG, OPS, WHIP, etc.
     */
    public static class PitchingStats {
        // Raw totals
        private int totalPitches = 0;
        private int totalBattersFaced = 0;

        private int totalSingles = 0;
        private int totalDoubles = 0;
        private int totalTriples = 0;
        private int totalHomeRuns = 0;

        private int totalWalks = 0;
        private int totalStrikeouts = 0;
        private int totalHitByPitch = 0;

        private int totalReachedOnError = 0;
        private int totalOutsInPlay = 0;
        private int totalTotalBases = 0;

        // ---- aggregation ----

        public void addSession(Session s) {
            totalPitches      += s.totalPitches();
            totalBattersFaced += s.battersFaced();

            totalSingles      += s.singles();
            totalDoubles      += s.doubles();
            totalTriples      += s.triples();
            totalHomeRuns     += s.homeRuns();

            totalWalks        += s.walks();
            totalStrikeouts   += s.strikeouts();
            totalHitByPitch   += s.hitByPitch();

            totalReachedOnError += s.reachedOnError();
            totalOutsInPlay     += s.outsInPlay();
            totalTotalBases     += s.totalBases();
        }

        // raw getters

        public int getTotalPitches()       { return totalPitches; }
        public int getTotalBattersFaced()  { return totalBattersFaced; }

        public int getTotalSingles()       { return totalSingles; }
        public int getTotalDoubles()       { return totalDoubles; }
        public int getTotalTriples()       { return totalTriples; }
        public int getTotalHomeRuns()      { return totalHomeRuns; }

        public int getTotalWalks()         { return totalWalks; }
        public int getTotalStrikeouts()    { return totalStrikeouts; }
        public int getTotalHitByPitch()    { return totalHitByPitch; }

        public int getTotalReachedOnError(){ return totalReachedOnError; }
        public int getTotalOutsInPlay()    { return totalOutsInPlay; }
        public int getTotalTotalBases()    { return totalTotalBases; }

        // derived

        //Hits = 1B + 2B + 3B + HR
        public int getHits() {
            return totalSingles + totalDoubles + totalTriples + totalHomeRuns;
        }

        //Outs = outs in play + strikeouts (in your current model).
        public int getOuts() {
            return totalOutsInPlay + totalStrikeouts;
        }

        /*
         AB (official at-bats) in this simplified model:
         AB = H + outs in play + strikeouts + reached on error
         (no sac flies modeled).
         */
        public int getAtBats() {
            return getHits() + totalOutsInPlay + totalStrikeouts + totalReachedOnError;
        }

        // innings and rate stats

        //Total outs as a fraction of innings, e.g., 17 outs = 5.666... IP.
        public double getInningsPitchedDecimal() {
            return getOuts() / 3.0;
        }

        //IP shown as "X.Y" where Y is remaining outs (0–2).
        public String getInningsPitchedString() {
            int outs = getOuts();
            int full = outs / 3;
            int rem  = outs % 3;
            return full + "." + rem;
        }

        //Batting average = H / AB. Returns NaN if AB = 0.
        public double getAvg() {
            int ab = getAtBats();
            if (ab == 0) return Double.NaN;
            return (double) getHits() / ab;
        }

        /*
         On-base % (simplified):
         (H + BB + HBP) / (AB + BB + HBP)
         */
        public double getObp() {
            int ab = getAtBats();
            int denom = ab + totalWalks + totalHitByPitch;
            if (denom == 0) return Double.NaN;
            return (double) (getHits() + totalWalks + totalHitByPitch) / denom;
        }

        //Slugging % = total bases / AB.
        public double getSlg() {
            int ab = getAtBats();
            if (ab == 0) return Double.NaN;
            return (double) totalTotalBases / ab;
        }

        // OPS = OBP + SLG (if both are defined).
        public double getOps() {
            double obp = getObp();
            double slg = getSlg();
            if (Double.isNaN(obp) || Double.isNaN(slg)) return Double.NaN;
            return obp + slg;
        }

        /*
         WHIP = (BB + H) / IP.
         Returns NaN if IP = 0.
         */
        public double getWhip() {
            double ip = getInningsPitchedDecimal();
            if (ip == 0.0) return Double.NaN;
            return (double) (totalWalks + getHits()) / ip;
        }
    }

    private final List<Session> sessions;

    // Use this when you have a Pitcher
    public StatCalculator(Pitcher pitcher) {
        this.sessions = new ArrayList<>(pitcher.getSessions());
    }

    // Or pass sessions directly (subsets, filters, etc.)
    public StatCalculator(List<Session> sessions) {
        this.sessions = new ArrayList<>(sessions);
    }

    /*
     Aggregate stats into buckets according to the grouping.

     grouping how to group sessions
     map: key = label ("Career", "2024", "2024-03"), value = stats for that bucket
     */
    public Map<String, PitchingStats> aggregate(Grouping grouping) {
        return switch (grouping) {
            case CAREER     -> aggregateCareer();
            case BY_YEAR    -> aggregateByYear();
            case BY_MONTH   -> aggregateByMonth();
            case BY_DAY     -> aggregateByDay();
        };
    }

    // internal aggregation helpers

    private Map<String, PitchingStats> aggregateCareer() {
        PitchingStats stats = new PitchingStats();
        for (Session s : sessions) {
            stats.addSession(s);
        }

        Map<String, PitchingStats> map = new LinkedHashMap<>();
        map.put("Career", stats);
        return map;
    }

    private Map<String, PitchingStats> aggregateByYear() {
        Map<Integer, PitchingStats> byYear = new HashMap<>();

        for (Session s : sessions) {
            LocalDate date = s.getTimestamp(); // you need this on Session
            int year = date.getYear();

            PitchingStats stats = byYear.computeIfAbsent(year, y -> new PitchingStats());
            stats.addSession(s);
        }

        // Sort years
        List<Integer> years = new ArrayList<>(byYear.keySet());
        Collections.sort(years);

        Map<String, PitchingStats> result = new LinkedHashMap<>();
        for (Integer year : years) {
            result.put(year.toString(), byYear.get(year));
        }
        return result;
    }

    private Map<String, PitchingStats> aggregateByMonth() {
        Map<YearMonth, PitchingStats> byMonth = new HashMap<>();

        for (Session s : sessions) {
            LocalDate date = s.getTimestamp();
            YearMonth ym = YearMonth.from(date);

            PitchingStats stats = byMonth.computeIfAbsent(ym, x -> new PitchingStats());
            stats.addSession(s);
        }

        // Sort YearMonth keys
        List<YearMonth> months = new ArrayList<>(byMonth.keySet());
        Collections.sort(months);

        Map<String, PitchingStats> result = new LinkedHashMap<>();
        for (YearMonth ym : months) {
            result.put(ym.toString(), byMonth.get(ym)); // e.g. "2025-11"
        }
        return result;
    }
    private Map<String, PitchingStats> aggregateByDay() {
        //TO DO
        return new LinkedHashMap<>();
    }
}
