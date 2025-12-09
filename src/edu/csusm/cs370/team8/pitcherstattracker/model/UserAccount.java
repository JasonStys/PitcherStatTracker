package edu.csusm.cs370.team8.pitcherstattracker.model;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;

import java.util.ArrayList;
import java.util.List;

/* Refers to a user that logs in, like a coach that has multiple pitchers they manage
 * An aggregation of Pitchers
 */
public class UserAccount {
    // Fields
    private final List<Pitcher> pitchers = new ArrayList<>();
    private String username;
    private String hashPass;

    // Ideally this should be a list of ID so Pitchers are nested inside UserAccounts
    // But instead linked in a many-to-many connection, but not viable under time.
    //private final List<Integer> pitchersID = new ArrayList<>();


    public UserAccount() {
        // Empty Constructor for Jackson
    }
    // Constructor that should actually be used. An account with null fields will cause save error messages
    public UserAccount(String user, String pass) {
        this.username = user;
        this.hashPass = pass;
    }
    // Getters. Setters not needed as we can't change these in this implementation.
    public String getUsername() {return username;}
    public String getHashPass() {return hashPass;}

    // Get list of pitchers
    public List<Pitcher> getPitchers() {
        return pitchers;
    }

    public void addPitcher(Pitcher pitcher) {
        pitchers.add(pitcher);
    }

    // Search for a specific Pitcher by ID
    public Pitcher getPitcher(int id) {
        for (Pitcher p : pitchers) {
            if (p.getID() == id) {
                return p;
            }
        }
        MainWindow.displayError("PROGRAM ERROR", "This UserAccount can't find that Pitcher!");
        return null;
    }

    // Save a specific pitcher by ID
    public boolean savePitcher(Pitcher newPitcher) {
        for (int i = 0; i < pitchers.size(); i++) {
            if (pitchers.get(i).getID() == newPitcher.getID()) {
                pitchers.set(i, newPitcher);
                return true;
            }
        }
        MainWindow.displayError("PROGRAM ERROR", "This UserAccount can't find that Pitcher!");
        return false;
    }
}
