package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.controller.PitcherStatsTable;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PitcherProfilePanel extends JPanel {
    private JButton backButton;
    private Pitcher ourPitcher;

    PitcherProfilePanel(Pitcher p) {
        this.ourPitcher = p;
        this.setLayout(new BorderLayout());
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        PitcherStatsTable statsTableController = new PitcherStatsTable(ourPitcher);
        TableWidget table = statsTableController.buildDefaultCareerTable();
        this.add(table, BorderLayout.CENTER);

        // North Header of button
        JPanel buttonHeader = new JPanel();
        //buttonHeader.setLayout(new GridLayout(1, 4));
        buttonHeader.setLayout(new BoxLayout(buttonHeader, BoxLayout.X_AXIS));
        backButton = new JButton("Back", IconGetter.BACK);
        backButton.setActionCommand("cancel");
        backButton.addActionListener(this::actionPerformed);

        buttonHeader.add(backButton);
        this.add(buttonHeader, BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "cancel":
                MainWindow.prevPanel();
                break;
        }
    }
}
