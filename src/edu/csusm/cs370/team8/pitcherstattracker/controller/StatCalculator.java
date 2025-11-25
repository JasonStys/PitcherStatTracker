package edu.csusm.cs370.team8.pitcherstattracker.controller;

import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;
import edu.csusm.cs370.team8.pitcherstattracker.model.Session;
import edu.csusm.cs370.team8.pitcherstattracker.view.TableWidget;

import java.util.List;

public class StatCalculator {
    List<Session> allSessions;

    // Constructor when given a Pitcher (works with ALL sessions for that pitcher)
    public StatCalculator(Pitcher pitcher) {
        this.allSessions =  pitcher.getSessions();
    }
    // Constructor when given a list of sessions
    public StatCalculator(List<Session> slist) {
        this.allSessions = slist;
    }
    // Will eventually add a constructor that takes in a pitcher and two timestamps,
    // but don't need that for figuring out table.

    public TableWidget getPitcherTableWidget() {

        int numPitches = 0;
        for (Session s : allSessions) { // iterate through every session this StatCalculator was given
            numPitches += s.getPitches().size(); // Count the total number of all pitches ever made.
        }

        // The columns on the top. I can't see an easy way to have columns on the left side,
        // but you're fine to do that as I might figure it out later.
        String[] columnNames = {"Number of pitches",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};

        // The data, row by row, starting with row 2. It's just a 2-dimensional array
        Object[][] rowData = {
                {numPitches, "Smith", "Snowboarding", 5, false},
                {"This", "Doe", "Rowing", 3, true},
                {"Other", "Black", "Knitting", 2, false},
                {"Data", "White", "Speed reading", 20, true},
                {"Means", "Brown", "Pool", 10, false},
                {"Nothing", "Brown", "Pool", 10, false}
        };

        rowData[2][2] = "Baking"; // You can also write to the table like this. This replaces "Knitting".

        // Make the table.
        TableWidget table = new TableWidget(columnNames, rowData);

        // Set the preferred width. Note the table stretches the entire screen so this is more a minimum.
        table.setColumnWidth(0, 85);
        table.setColumnWidth(1, 30);
        table.setColumnWidth(2, 50);
        table.setColumnWidth(3, 30);
        table.setColumnWidth(4, 30);

        // Returns the table for display
        return table;
    }
}
