package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.ArrayList;

/* This represents a single "Game" or "Period of training", usually 1-2 hours.
 * THIS CLASS IS WHAT HAS ALL THE STAT FIELDS.
 */
public class Session {
    // Needs a Timestamp but I need to look at what's the best java module to use for that.
    ArrayList<Pitch> pitch; // All the different games and trainings this pitcher has done.
}
