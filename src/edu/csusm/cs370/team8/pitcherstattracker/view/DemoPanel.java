package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.controller.RandomDataCreator;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;
import edu.csusm.cs370.team8.pitcherstattracker.model.Session;
import edu.csusm.cs370.team8.pitcherstattracker.model.UserAccount;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.util.Random;

// This panel was for stubbing out other panel functionality with random data, before full navigation existed.
// Can be seen if MainWindow.DEMO_MODE is set to true,
// but may cause errors/bugs with the saving since it skips the login steps the program expects.

public class DemoPanel extends JPanel implements TitledPanel {
    private JButton buttonOne;
    private JButton buttonTwo;
    private JButton buttonThree;
    private JButton buttonFour;
    private JButton buttonFive;
    private JButton buttonSix;

    private final String TITLE = "Demo Panel for Testing";
    public String getTitle() {return TITLE;}

    public DemoPanel() {
        this.setLayout(new BorderLayout());
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        // Panel that holds 6 buttons.
        JPanel centerPanel = new JPanel(new GridLayout(3, 2));

        // Label on the top.
        JLabel labelOne = new JLabel("<html>Demo Menu.<br>Click a button to launch that screen</html>");
        labelOne.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(labelOne,  BorderLayout.NORTH);

        // Self explanatory buttons
        buttonOne = new JButton("Open Session Editor with random data");
        buttonOne.setActionCommand("one");
        buttonOne.addActionListener(this::actionPerformed);
        centerPanel.add(buttonOne);

        buttonTwo = new JButton("Open Table Display with random data");
        buttonTwo.setActionCommand("two");
        buttonTwo.addActionListener(this::actionPerformed);
        centerPanel.add(buttonTwo);

        buttonThree = new JButton("Open Pitcher's list of sessions");
        buttonThree.setActionCommand("three");
        buttonThree.addActionListener(this::actionPerformed);
        centerPanel.add(buttonThree);

        buttonFour = new JButton("Open Coach List");
        buttonFour.setActionCommand("four");
        buttonFour.addActionListener(this::actionPerformed);
        centerPanel.add(buttonFour);

        buttonFive = new JButton("Open Coach Table");
        buttonFive.setActionCommand("five");
        buttonFive.addActionListener(this::actionPerformed);
        centerPanel.add(buttonFive);

        buttonSix = new JButton("Open Login");
        buttonSix.setActionCommand("six");
        buttonSix.addActionListener(this::actionPerformed);
        centerPanel.add(buttonSix);

        centerPanel.setPreferredSize(new Dimension(600, 300));
        this.add(centerPanel, BorderLayout.CENTER);
    }

    // Again this is randomly created stubbed panels. Not in the final release version of program.
    public void actionPerformed(ActionEvent e) {
        Random random = new Random();
        int numSessions = random.nextInt(15,91);
        Pitcher examplePitcher;
        UserAccount exampleAccount;
        switch (e.getActionCommand()) {
            case "one":
                Session exampleSession = RandomDataCreator.createRandomSessionData(15);
                SessionEditPanel sesEdit = new SessionEditPanel(exampleSession);
                MainWindow.switchPanel(sesEdit);
                break;

            case "two":
                examplePitcher = RandomDataCreator.createRandomPitcher(numSessions);
                PitcherProfilePanel profilePanel = new PitcherProfilePanel(examplePitcher);
                MainWindow.switchPanel(profilePanel);
                break;

            case "three":
                examplePitcher = RandomDataCreator.createRandomPitcher(numSessions);
                SessionListPanel sessionListPanel = new SessionListPanel(examplePitcher);
                MainWindow.switchPanel(sessionListPanel);
                break;

            case "four":
                UserAccount account = RandomDataCreator.generateCoach();
                //UserAccount account = MainWindow.getUser();
                PitcherListPanel plistPanel = new PitcherListPanel(account);
                MainWindow.switchPanel(plistPanel);
                break;

            case "five":
                exampleAccount = RandomDataCreator.generateCoach();
                List<Pitcher> examplePitchers = exampleAccount.getPitchers();
                AllPitcherDisplayPanel coachTablePanel = new AllPitcherDisplayPanel(examplePitchers);
                MainWindow.switchPanel(coachTablePanel);
                break;

            case "six":
                MainWindow.switchPanel(new LoginPanel());
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR", "This button is not assigned to any code!");
                break;

        }
    }
}
