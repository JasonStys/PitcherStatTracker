package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.controller.StringCryptographer;
import edu.csusm.cs370.team8.pitcherstattracker.dao.UserAccountDao;
import edu.csusm.cs370.team8.pitcherstattracker.model.UserAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoginPanel extends JPanel implements TitledPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

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
        usernameField = new JTextField("coachcoacherson",30);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.setPreferredSize(new Dimension(50,30));

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        JLabel passwordLabel = new JLabel(" Password: ");
        passwordField = new JPasswordField("supersecure",30);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.setPreferredSize(new Dimension(50,30));

        JPanel buttonPanel = new JPanel(new  FlowLayout(FlowLayout.RIGHT));
        loginButton = new JButton("Login");
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this::actionPerformed);
        registerButton = new JButton("Register");
        registerButton.setActionCommand("register");
        registerButton.addActionListener(this::actionPerformed);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        centerPanel.add(usernamePanel);
        centerPanel.add(passwordPanel);
        centerPanel.add(buttonPanel);

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
        UserAccount account;
        // Both buttons use this part of code
        String username = usernameField.getText();
        String filename = StringCryptographer.sanitizeFilename(username);
        filename = filename + ".json";
        Path dataFile = Paths.get("data",filename);
        String hashPass = StringCryptographer.getPassHash(username,
                new String(passwordField.getPassword()));

        switch (e.getActionCommand()) {
            case "login":
                if (Files.exists(dataFile)) {
                    account = MainWindow.loadAccount(dataFile);
                    if (account.getHashPass().equals(hashPass)) {
                        PitcherListPanel plistPanel = new PitcherListPanel(account);
                        MainWindow.switchPanel(plistPanel);
                        break;
                    }
                }
                MainWindow.displayError("Login Invalid", "Username and/or Password is incorrect.");
                break;

            case "register":
                int answer = MainWindow.displayYesNoCancel("Confirm Registration",
                        "<html>" +
                                "Would you like to create a new account<br>" +
                                "with this username and password?" +
                                "</html>");

                if (answer == 1) { // "Yes" is chosen.
                    if (Files.exists(dataFile)) {
                        MainWindow.displayError("Account already exists",
                                "An account with this name already exists.");
                    } else {
                        account = new UserAccount(username, hashPass);
                        MainWindow.loadNewAccount(dataFile, account);
                    }
                }
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR", "This button is not assigned to any code!");

        }
    }
}
