package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Refers to a pitcher with which there are sessions of stats for. NOT a user that logs in.
 *
 */
public class Pitcher {
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
            "Average Anthony", "Average Alison",
            "Terrible Tony", "Terrible Tina",
            "Exceptional Elliot", "Exceptional Elena",
            "Okay O'reilly", "Okay Oliva",
            "Standout Stanley", "Standout Savanna"});

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
