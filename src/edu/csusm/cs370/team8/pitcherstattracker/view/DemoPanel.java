package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;
import edu.csusm.cs370.team8.pitcherstattracker.model.Session;
import edu.csusm.cs370.team8.pitcherstattracker.model.UserAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class DemoPanel extends JPanel implements TitledPanel {
    private JButton buttonOne;
    private JButton buttonTwo;
    private JButton buttonThree;
    private JButton buttonFour;

    private final String TITLE = "Demo Panel for Testing";
    public String getTitle() {return TITLE;}

    public DemoPanel() {
        this.setLayout(new GridLayout(3, 2));
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
        //this.add(buttonOne);

        buttonTwo = new JButton("Open Table Display with random data");
        buttonTwo.setActionCommand("two");
        buttonTwo.addActionListener(this::actionPerformed);
        //this.add(buttonTwo);

        buttonThree = new JButton("Open Pitcher's list of sessions");
        buttonThree.setActionCommand("three");
        buttonThree.addActionListener(this::actionPerformed);
        //this.add(buttonThree);

        buttonFour = new JButton("Open Coach Dashboard");
        buttonFour.setActionCommand("four");
        buttonFour.addActionListener(this::actionPerformed);
        this.add(buttonFour);

        this.setPreferredSize(new Dimension(600, 300));
    }

    public void actionPerformed(ActionEvent e) {
        Random random = new Random();
        int numSessions = random.nextInt(15,91);
        Pitcher examplePitcher = Pitcher.createRandomPitcher(numSessions);
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

            case "four":
                UserAccount account = UserAccount.generateCoach();
                PitcherListPanel panel = new PitcherListPanel(account);
                MainWindow.switchPanel(panel);
                break;

        }
    }
}
