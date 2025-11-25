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

    public DemoPanel() {
        this.setLayout(new GridLayout(3, 1));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        JLabel labelOne = new JLabel("Demo Menu. Click a button to launch that screen");
        labelOne.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(labelOne);

        buttonOne = new JButton("Open Session Editor");
        buttonOne.setActionCommand("one");
        buttonOne.addActionListener(this::actionPerformed);
        this.add(buttonOne);

        buttonTwo = new JButton("Open Table Display");
        buttonTwo.setActionCommand("two");
        buttonTwo.addActionListener(this::actionPerformed);
        this.add(buttonTwo);

        this.setPreferredSize(new Dimension(400, 200));
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "one":
                Session exampleSession = Session.createRandomSessionData(15);
                SessionEditPanel sesEdit = new SessionEditPanel(exampleSession);
                MainWindow.switchPanel(sesEdit);
                break;

            case "two":
                Pitcher examplePitcher = Pitcher.createRandomPitcher(15,15);
                PitcherProfilePanel profilePanel = new PitcherProfilePanel(examplePitcher);
                MainWindow.switchPanel(profilePanel);
                break;

        }
    }
}
