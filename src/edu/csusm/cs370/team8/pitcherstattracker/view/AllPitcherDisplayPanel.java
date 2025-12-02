package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.controller.PitcherStatsTable;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AllPitcherDisplayPanel extends JPanel implements TitledPanel {
    private JButton backButton;
    private final List<Pitcher> ourPitchers;

    private final String TITLE = "Coach Overview";
    public String getTitle() {return TITLE;}

    public AllPitcherDisplayPanel(List<Pitcher> ourPitchers) {
        this.ourPitchers = ourPitchers;
        this.setLayout(new BorderLayout());
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        ourPitchers.sort(Pitcher.WhipComparator); // Sorts all pitchers based on their WHIP
        PitcherStatsTable statsTableController = new PitcherStatsTable(ourPitchers);
        TableWidget table = statsTableController.buildMultiTable();
        table.setPreferredSize(new Dimension(700, 200));
        this.add(table, BorderLayout.CENTER);

        JPanel labelPanel = new JPanel();
        labelPanel.setBorder(BorderFactory.createTitledBorder("Glossary:"));

        StringBuilder sbLeft = new StringBuilder();
        sbLeft.append("<html>");
        sbLeft.append("IP = Innings Pitched").append("<br>");
        sbLeft.append("BF = Batters Faced").append("<br>");
        sbLeft.append("P = Pitches").append("<br>");
        sbLeft.append("H = Hits").append("<br>");
        sbLeft.append("BB = Walks (Base on Balls)");
        sbLeft.append("</html>");
        JLabel labelsLeft = new JLabel(sbLeft.toString());
        labelsLeft.setHorizontalAlignment(SwingConstants.LEFT);

        StringBuilder sbRight = new StringBuilder();
        sbRight.append("<html>");
        sbRight.append("K = Strikeouts").append("<br>");
        sbRight.append("AVG = Batting Average Against").append("<br>");
        sbRight.append("OBP = On Base Percentage Against").append("<br>");
        sbRight.append("SLG = Slugging Against").append("<br>");
        sbRight.append("WHIP = Walks + Hits / Innings Pitched");
        sbRight.append("</html>");
        JLabel labelsRight = new JLabel(sbRight.toString());
        labelsRight.setHorizontalAlignment(SwingConstants.LEFT);

        labelPanel.setLayout(new GridLayout(1,2));
        labelPanel.add(labelsLeft);
        labelPanel.add(labelsRight);
        this.add(labelPanel, BorderLayout.SOUTH);

        // North Header of button
        JPanel header = new JPanel();
        //header.setLayout(new GridLayout(1, 4));
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        backButton = new JButton("Back", IconGetter.BACK);
        backButton.setActionCommand("back");
        backButton.addActionListener(this::actionPerformed);
        header.add(backButton);

        JPanel topLabelPanel =  new JPanel();
        StringBuilder sbTop = new StringBuilder();
        sbTop.append("<html>");
        sbTop.append("Sorted by WHIP: How many bases awarded per inning.").append("<br>");
        sbTop.append("(The lower the number, the better the pitcher, approximately.)");
        sbTop.append("</html>");
        JLabel labelTop = new JLabel(sbTop.toString());
        topLabelPanel.add(labelTop);
        header.add(topLabelPanel);

        this.add(header, BorderLayout.NORTH);
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
