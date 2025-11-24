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

}
