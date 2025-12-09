package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.controller.PitcherStatsTable;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// This displays a table for a specific pitcher, showing yearly stats.
// If we had more time, would have more complex stuff here, such as monthly stats and graphical info.
public class PitcherProfilePanel extends JPanel implements TitledPanel {
    private JButton backButton;
    private Pitcher ourPitcher;

    private final String TITLE = "Pitcher Profile: ";
    public String getTitle() {return TITLE + ourPitcher.getName();}

    // Constructor: Panels needs a pitcher to work with
    public PitcherProfilePanel(Pitcher p) {
        this.ourPitcher = p;
        this.setLayout(new BorderLayout());
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        // Center table.
        PitcherStatsTable statsTableController = new PitcherStatsTable(ourPitcher);
        TableWidget table = statsTableController.buildDefaultTable();
        table.setPreferredSize(new Dimension(500, 150));
        this.add(table, BorderLayout.CENTER);

        // Add glossary to bottom
        this.add(WidgetGetter.Glossary(), BorderLayout.SOUTH);

        // North Header of button and name
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        // Back Button
        backButton = new JButton("Back", IconGetter.BACK);
        backButton.setActionCommand("back");
        backButton.addActionListener(this::actionPerformed);
        header.add(backButton);
        // Name of the Pitcher
        JPanel namePane = new JPanel();
        namePane.setBorder(BorderFactory.createTitledBorder(
                "Name:"));
        JLabel nameLabel = new JLabel(ourPitcher.getName());
        namePane.add(nameLabel);
        header.add(namePane);

        this.add(header, BorderLayout.NORTH); // Add the header to the top
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "back":
                MainWindow.prevPanel();
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR","This button is not assigned to any code!");
        }
    }
}
