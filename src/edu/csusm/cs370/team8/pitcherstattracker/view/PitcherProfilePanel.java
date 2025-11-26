package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.controller.PitcherStatsTable;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PitcherProfilePanel extends JPanel implements TitledPanel {
    private JButton backButton;
    private Pitcher ourPitcher;

    private final String TITLE = "Pitcher Profile: ";
    public String getTitle() {return TITLE + ourPitcher.getName();}

    PitcherProfilePanel(Pitcher p) {
        this.ourPitcher = p;
        this.setLayout(new BorderLayout());
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        PitcherStatsTable statsTableController = new PitcherStatsTable(ourPitcher);
        TableWidget table = statsTableController.buildDefaultTable();
        table.setPreferredSize(new Dimension(500, 150));
        this.add(table, BorderLayout.CENTER);

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("IP = Innings Pitched").append("<br>");
        sb.append("BF = Batters Faced").append("<br>");
        sb.append("P = Pitches").append("<br>");
        sb.append("H = Hits").append("<br>");
        sb.append("BB = Walks (Base on Balls)").append("<br>");
        sb.append("K = Strikeouts").append("<br>");
        sb.append("AVG = Batting Average Against").append("<br>");
        sb.append("OBP = On Base Percentage Against").append("<br>");
        sb.append("SLG = Slugging Against").append("<br>");
        sb.append("WHIP = Walks + Hits / Innings Pitched");
        sb.append("</html>");
        JLabel labels = new JLabel(sb.toString());
        labels.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(labels, BorderLayout.SOUTH);

        // North Header of button
        JPanel header = new JPanel();
        //header.setLayout(new GridLayout(1, 4));
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        backButton = new JButton("Back", IconGetter.BACK);
        backButton.setActionCommand("cancel");
        backButton.addActionListener(this::actionPerformed);
        header.add(backButton);

        JPanel namePane = new JPanel();
        namePane.setBorder(BorderFactory.createTitledBorder(
                "Name:"));
        JLabel nameLabel = new JLabel(ourPitcher.getName());
        namePane.add(nameLabel);
        header.add(namePane);

        this.add(header, BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "cancel":
                MainWindow.prevPanel();
                break;
        }
    }
}
