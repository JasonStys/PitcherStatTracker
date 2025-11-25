package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;
import edu.csusm.cs370.team8.pitcherstattracker.model.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DemoPanel extends JPanel {
    private JButton buttonOne;
    private JButton buttonTwo;
    private JButton buttonThree;

    public DemoPanel() {
        this.setLayout(new GridLayout(2, 2));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        JLabel labelOne = new JLabel("<html>Demo Menu.<br>Click a button to launch that screen</html>");
        labelOne.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(labelOne);

        buttonOne = new JButton("Open Session Editor with random data");
        buttonOne.setActionCommand("one");
        buttonOne.addActionListener(this::actionPerformed);
        this.add(buttonOne);

        buttonTwo = new JButton("Open Table Display with random data");
        buttonTwo.setActionCommand("two");
        buttonTwo.addActionListener(this::actionPerformed);
        this.add(buttonTwo);

        buttonThree = new JButton("Open Pitcher's list of sessions");
        buttonThree.setActionCommand("three");
        buttonThree.addActionListener(this::actionPerformed);
        this.add(buttonThree);

        this.setPreferredSize(new Dimension(600, 200));
    }

    public void actionPerformed(ActionEvent e) {
        Pitcher examplePitcher = Pitcher.createRandomPitcher(15,15);
        switch (e.getActionCommand()) {
            case "one":
                Session exampleSession = Session.createRandomSessionData(15);
                SessionEditPanel sesEdit = new SessionEditPanel(exampleSession);
                MainWindow.switchPanel(sesEdit);
                break;

            case "two":
                PitcherProfilePanel profilePanel = new PitcherProfilePanel(examplePitcher);
                MainWindow.switchPanel(profilePanel);
                break;

            case "three":
               SessionListPanel sessionListPanel = new SessionListPanel(examplePitcher);
               MainWindow.switchPanel(sessionListPanel);
               break;

        }
    }
}
