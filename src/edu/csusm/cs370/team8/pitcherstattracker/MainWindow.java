package edu.csusm.cs370.team8.pitcherstattracker;

import javax.swing.*;
import java.awt.event.*;

import edu.csusm.cs370.team8.pitcherstattracker.model.*;
import edu.csusm.cs370.team8.pitcherstattracker.controller.*;
import edu.csusm.cs370.team8.pitcherstattracker.view.*;

/* This might just be more program startup stuff and we'll have a separate
 * "GraphicInterface" class that handles the window that the user interacts with.
 */

public class MainWindow {
    private static JFrame window = new JFrame("Perfect Pitch");
    private static JPanel currPanel; // Current menu in the window
    private static JPanel prevPanel; // Stores previous menu in case user backtracks

    public static void main(String[] args) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        InputPanel input = new InputPanel();
        switchPanel(input);

        window.setVisible(true);
    }

    public static void switchPanel(JPanel newPanel) {
        prevPanel = currPanel;
        if (currPanel != null) {
            window.remove(currPanel);
        }
        currPanel = newPanel;
        window.add(newPanel);
        window.pack();
        //window.setSize(800, 600);
    }
}
