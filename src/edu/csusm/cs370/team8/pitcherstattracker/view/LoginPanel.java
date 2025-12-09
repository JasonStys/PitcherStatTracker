package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.controller.RelativePathGetter;
import edu.csusm.cs370.team8.pitcherstattracker.controller.StringCryptographer;
import edu.csusm.cs370.team8.pitcherstattracker.model.UserAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// The Login Screen.
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
        // Create panel to manage the size.
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Username input. Created with default "coachcoacherson" for quick testing.
        // In a non-demonstrative program we would not have a default here or have it remember the last logged username.
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        JLabel usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField("coachcoacherson",30);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.setPreferredSize(new Dimension(50,30));

        // Password input. Created with default "supersecure" for quick testing.
        // A non-demonstrative program would never have anything here.
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        JLabel passwordLabel = new JLabel(" Password: ");
        passwordField = new JPasswordField("supersecure",30);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.setPreferredSize(new Dimension(50,30));

        // Login and Register buttons.
        JPanel buttonPanel = new JPanel(new  FlowLayout(FlowLayout.RIGHT));
        loginButton = new JButton("Login");
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this::actionPerformed);
        registerButton = new JButton("Register");
        registerButton.setActionCommand("register");
        registerButton.addActionListener(this::actionPerformed);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add all the components to our center panel.
        centerPanel.add(usernamePanel);
        centerPanel.add(passwordPanel);
        centerPanel.add(buttonPanel);

        // Fix its size.
        centerPanel.setPreferredSize(new Dimension(300, 100));
        centerPanel.setMinimumSize(new Dimension(300, 100));
        centerPanel.setMaximumSize(new Dimension(300, 100));

        this.add(centerPanel);

        // If we loaded the sticky.png resource, also display that.
        if (IconGetter.STICKY != null) {
            JLabel sticky = new JLabel(IconGetter.STICKY);
            this.add(sticky);
        }

        this.setPreferredSize(new Dimension(600, 300));
    }

    private void actionPerformed(ActionEvent e) {
        UserAccount account;
        // Both buttons use this part of code.
        // Get the filename that would be used from the sanitized username
        String username = usernameField.getText();
        String filename = StringCryptographer.sanitizeFilename(username);
        filename = filename + ".json";
        Path dataFile = Paths.get("data", filename);
        // If data directory not found, we are .jar file instead of Intellij, so get path differently.
        if (!Files.isDirectory(Paths.get("data"))) {
            try {
                dataFile = Paths.get(RelativePathGetter.getPath(), filename);
            } catch (Exception ex) {}
        }
        if (!Files.exists(dataFile)) {

        }
        // and also the hashpass that would be used by interweaving the encrypted hashes of both input fields.
        String hashPass = StringCryptographer.getPassHash(username,
                new String(passwordField.getPassword()));

        switch (e.getActionCommand()) {
            case "login":
                if (Files.exists(dataFile)) {
                    // Load account into singleton if it exists
                    account = MainWindow.loadAccount(dataFile);
                    if (account.getHashPass().equals(hashPass)) {
                        // Open up the dashboard for that account if the hashpass matches
                        PitcherListPanel plistPanel = new PitcherListPanel(account);
                        MainWindow.switchPanel(plistPanel);
                        break;
                    }
                }
                // Either the file didn't exist or the hashpass did not match.
                MainWindow.displayError("Login Invalid", "Username and/or Password is incorrect.");
                break;

            case "register":
                // Display popup and get resulting answer.
                int answer = MainWindow.displayYesNoCancel("Confirm Registration",
                        "<html>" +
                                "Would you like to create a new account<br>" +
                                "with this username and password?" +
                                "</html>");

                if (answer == 1) { // "Yes" is chosen.
                    if (Files.exists(dataFile)) {
                        // Someone with that username is already saved.
                        MainWindow.displayError("Account already exists",
                                "An account with this name already exists.");
                    } else {
                        // Create a new account with the inputs, and load it as a new account so the file is immediately created.
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
