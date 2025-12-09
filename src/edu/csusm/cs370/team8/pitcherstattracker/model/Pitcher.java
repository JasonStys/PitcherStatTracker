package edu.csusm.cs370.team8.pitcherstattracker.model;

import edu.csusm.cs370.team8.pitcherstattracker.controller.StatCalculator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/* Refers to a pitcher with which there are sessions of stats for. NOT a user that logs in.
 * It is composed of Sessions.
 */
public class Pitcher implements Comparable<Pitcher> {
    // Fields
    private final List<Session> sessions = new ArrayList<>();
    private String name;

    // IDs of Pitchers
    private int id;
    private static int nextID = 0;
    public Pitcher() {
        id = nextID++;
    }

    // Getters and Setters
    public int getID() {return id;}
    public void setID(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    //A list of all their sessions so far. A copy so will need to be updated.
    public List<Session> getSessions() {return List.copyOf(sessions);}
    // Erases and sets the session list of a Pitcher
    public void setSessions(List<Session> s) {
        this.sessions.clear();
        this.sessions.addAll(s);
        sessions.sort(null); // Sorts by Timestamp
    }
    public void addSession(Session session) {
        sessions.add(session);
        sessions.sort(null); // Sorts by timestamp
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

    // toString() is what JList prints in the panels.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);

        return sb.toString();
    }
}
