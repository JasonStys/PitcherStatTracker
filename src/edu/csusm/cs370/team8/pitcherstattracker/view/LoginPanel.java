package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.UserAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel implements TitledPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private final String TITLE = "Login";
    public String getTitle() {return TITLE;}

    public LoginPanel() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        JLabel usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(30);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.setPreferredSize(new Dimension(50,30));

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        JLabel passwordLabel = new JLabel(" Password: ");
        passwordField = new JPasswordField(30);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.setPreferredSize(new Dimension(50,30));

        loginButton = new JButton("Login");
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this::actionPerformed);

        centerPanel.add(usernamePanel);
        centerPanel.add(passwordPanel);
        centerPanel.add(loginButton);
        centerPanel.setPreferredSize(new Dimension(300, 100));
        centerPanel.setMinimumSize(new Dimension(300, 100));
        centerPanel.setMaximumSize(new Dimension(300, 100));

        this.add(centerPanel);

        if (IconGetter.STICKY != null) {
            JLabel sticky = new JLabel(IconGetter.STICKY);
            this.add(sticky);
        }

        this.setPreferredSize(new Dimension(600, 300));
    }

    private void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "login":
                if (usernameField.getText().equals("coachcoacherson")
                        && passwordField.getText().equals("supersecure")) {
                    UserAccount exampleAccount = UserAccount.generateCoach();
                    PitcherListPanel plistPanel = new PitcherListPanel(exampleAccount);
                    MainWindow.switchPanel(plistPanel);
                } else {
                    MainWindow.displayError("Login Invalid", "Username and/or Password is incorrect.");
                }
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR", "This button is not assigned to any code!");

        }
    }
}
