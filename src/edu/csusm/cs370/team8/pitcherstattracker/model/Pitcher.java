package edu.csusm.cs370.team8.pitcherstattracker.model;

import edu.csusm.cs370.team8.pitcherstattracker.model.Session;
import edu.csusm.cs370.team8.pitcherstattracker.controller.StatCalculator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/* Refers to a pitcher with which there are sessions of stats for. NOT a user that logs in.
 *
 */
public class Pitcher implements Comparable<Pitcher> {
    private final List<Session> sessions = new ArrayList<>();
    private String name;
    private int id;
    private static int nextID = 0;

    public Pitcher() {
        id = nextID++;
    }
    public int getID() {return id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    //A list of all their sessions so far. A copy so will need to be updated.
    public List<Session> getSessions() {return List.copyOf(sessions);}
    public void setSessions(List<Session> s) {
        this.sessions.clear();
        this.sessions.addAll(s);
        sessions.sort(null);
    }
    public void addSession(Session session) {
        sessions.add(session);
        sessions.sort(null);
    }

    // Default sort on a list of pitchers is by name.
    @Override
    public int compareTo(Pitcher o) {
        return this.getName().compareTo(o.getName());
    }

    // Allows Pitchers to be compared based on their WHIP stat.
    // Those with the lowest are first, which is roughly how good the pitcher is.
    public static Comparator<? super Pitcher> WhipComparator = new WhipCompare();
    public static class WhipCompare implements Comparator<Pitcher> {
        @Override
        public int compare(Pitcher p1, Pitcher p2) {
            StatCalculator mine = new StatCalculator(p1);
            mine.calcStats();
            Double myDouble = mine.getWhip();

            StatCalculator theirs = new StatCalculator(p2);
            theirs.calcStats();
            Double theirDouble = theirs.getWhip();

            // NaN (from an incalculable WHIP stat) is considered larger than all other doubles,
            // which is exactly what we want. This puts any pitcher with not enough data after all others.
            return myDouble.compareTo(theirDouble);
        }
    }

    public static Pitcher createRandomPitcher(int numSessions) {
        Random random =  new Random();
        int numPitches;
        Pitcher pitcher = new Pitcher();
        for (int i = 0; i < numSessions; i++) {
            numPitches = random.nextInt(15, 91);
            pitcher.addSession(Session.createRandomSessionData(numPitches));
        }
        pitcher.setName(getRandomName());
        return pitcher;
    }

    // For random generation
    private static final List<String> exampleNames = List.of(new String[]{
            "Unfathomable Ulysses",
            "Standout Savanna",
            "Exceptional Elliot",
            "Suitable Sally",
            "Decent Darren",
            "Average Alison",
            "Okay O'reilly",
            "Sketchy Steven",
            "Terrible Tony",
            "Blind Bartholomew",
            "Placeholder Platon"});

    public static String getRandomName() {
        Random random = new Random();
        return exampleNames.get(random.nextInt(exampleNames.size()));
    }

    public static String getNextName(int i) {
        return exampleNames.get(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);

        return sb.toString();
    }
}
