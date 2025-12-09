package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.controller.PitcherStatsTable;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// This panel shows a table of a collection of Pitchers by their career stats
public class AllPitcherDisplayPanel extends JPanel implements TitledPanel {
    private JButton backButton;
    private final List<Pitcher> ourPitchers;

    private final String TITLE = "Coach Overview";
    public String getTitle() {return TITLE;}

    // Constructor: This panel needs a list of Pitchers to function.
    public AllPitcherDisplayPanel(List<Pitcher> ourPitchers) {
        this.ourPitchers = ourPitchers;
        this.setLayout(new BorderLayout());
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        // Center table
        ourPitchers.sort(Pitcher.WhipComparator); // Sorts all pitchers based on their WHIP
        PitcherStatsTable statsTableController = new PitcherStatsTable(ourPitchers);
        TableWidget table = statsTableController.buildMultiTable();
        table.setPreferredSize(new Dimension(700, 200));
        this.add(table, BorderLayout.CENTER);

        // Glossary on the top.
        this.add(WidgetGetter.Glossary(), BorderLayout.SOUTH);

        // North Header with Back button
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        backButton = new JButton("Back", IconGetter.BACK);
        backButton.setActionCommand("back");
        backButton.addActionListener(this::actionPerformed);
        header.add(backButton);

        // Add text to the top next to Back button
        JPanel topLabelPanel =  new JPanel();
        StringBuilder sbTop = new StringBuilder();
        sbTop.append("<html>");
        sbTop.append("Sorted by WHIP: How many bases awarded per inning.").append("<br>");
        sbTop.append("(The lower the number, the better the pitcher, approximately.)");
        sbTop.append("</html>");
        JLabel labelTop = new JLabel(sbTop.toString());
        topLabelPanel.add(labelTop);
        header.add(topLabelPanel);

        // Add the header.
        this.add(header, BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "back":
                MainWindow.prevPanel(); // Return to previous screen
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR","This button is not assigned to any code!");
        }
    }
}
