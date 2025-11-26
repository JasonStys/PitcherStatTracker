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
        this.add(table, BorderLayout.CENTER);

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
