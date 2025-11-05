package edu.csusm.cs370.team8.pitcherstattracker;

import edu.csusm.cs370.team8.pitcherstattracker.model.*;
import edu.csusm.cs370.team8.pitcherstattracker.controller.*;
import edu.csusm.cs370.team8.pitcherstattracker.view.*;

/* This might just be more program startup stuff and we'll have a separate
 * "GraphicInterface" class that handles the window that the user interacts with.
 */

public class MainWindow {
    Pitcher pitcher; // Ignore this, just testing the package stuff.
    Session session1;

    // Constructor for MainWindow
    public MainWindow() {
        session1 = new Session(101);

        // Create and add new Pitch objects to this session
        session1.addPitch(Pitch.Type.Fastball, Pitch.Result.CalledStrike, true, 96.4);
        session1.addPitch(Pitch.Type.Curveball, Pitch.Result.Foul, false, 79.2);

        // View all pitches in the session
        session1.showPitches();
    }

    // main method to actually run it
    public static void main(String[] args) {
        new MainWindow();  // creates and runs a new MainWindow
    }
}
