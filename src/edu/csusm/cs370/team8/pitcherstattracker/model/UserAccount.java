package edu.csusm.cs370.team8.pitcherstattracker.model;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Refers to a user that logs in, like a coach that has multiple pitchers they manage
 *
 */
public class UserAccount {
    private final List<Pitcher> pitchers = new ArrayList<>();

    // Constructor
    public UserAccount() {

    }

    public List<Pitcher> getPitchers() {return pitchers;}
    public void addPitcher(Pitcher pitcher) {
        pitchers.add(pitcher);
    }

    public Pitcher getPitcher(int id) {
        for (Pitcher p : pitchers) {
            if (p.getID() == id) {return p;}
        }
        MainWindow.displayError("PROGRAM ERROR","This UserAccount can't find that Pitcher!");
        return null;
    }

    public boolean savePitcher(Pitcher newPitcher) {
        for (int i = 0; i<pitchers.size(); i++) {
            if (pitchers.get(i).getID() == newPitcher.getID()) {
                pitchers.set(i, newPitcher);
                return true;
            }
        }
        MainWindow.displayError("PROGRAM ERROR","This UserAccount can't find that Pitcher!");
        return false;
    }

    public static UserAccount generateCoach() {
        UserAccount account = new UserAccount();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int numSessions = random.nextInt(15,91);
            Pitcher examplePitcher = Pitcher.createRandomPitcher(numSessions);
            examplePitcher.setName(Pitcher.getNextName(i));
            account.addPitcher(examplePitcher);
        }
        return account;
    };
}
