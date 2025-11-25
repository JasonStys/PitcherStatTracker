package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.ArrayList;
import java.util.List;

/* Refers to a pitcher with which there are sessions of stats for. NOT a user that logs in.
 *
 */
public class Pitcher {
    private final List<Session> sessions = new ArrayList<>();

    //A list of all their sessions so far. A copy so will need to be updated.
    public List<Session> getSessions() {return List.copyOf(sessions);}
    public void addSession(Session session) {
        sessions.add(session);
        sessions.sort(null);
    }


    public static Pitcher createRandomPitcher(int numSessions, int numPitchesPerSession) {
        Pitcher pitcher = new Pitcher();
        for (int i = 0; i < numSessions; i++) {
            pitcher.addSession(Session.createRandomSessionData(numPitchesPerSession));
        }
        return pitcher;
    }

}
