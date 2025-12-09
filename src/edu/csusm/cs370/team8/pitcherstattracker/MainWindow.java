package edu.csusm.cs370.team8.pitcherstattracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;

import java.nio.file.Path;

import edu.csusm.cs370.team8.pitcherstattracker.dao.UserAccountDao;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;
import edu.csusm.cs370.team8.pitcherstattracker.view.*;

/* This represents the JFrame that is the main window, with static methods to handle navigation
 * It also holds the singleton to the account we are currently logged into.
 */

public class MainWindow {
    // Launches demo mode instead of login. For testing stubbed panels with random data.
    // This likely causes bugs now that database saving is implemented!
    private static boolean DEMO_MODE = false;

    private static final String PROGRAM_TITLE = "Perfect Pitch";
    private static JFrame window = new JFrame(PROGRAM_TITLE);
    private static JPanel currPanel; // Current menu in the window
    private static Deque<JPanel> panelHistory = new ArrayDeque<>(); // Used as a stack, with .push() and .pop() methods.

    // When a popup form appears, it is in this separate JPanel and JFrame
    private static JFrame popupWindow;
    private static PopPanel popupPanel;

    private static JFrame errorWindow = new JFrame(); // Does not need any further setup as JOptionPanel handles it.
    // errorWindow is also used for confirmation popup windows.

    // DAO / persistence fields
    private static UserAccountDao accountDao;
    private static UserAccount account;
    public static UserAccount getUser() {return account;}

    // Load an account from a filepath into our singleton field, and return that account object.
    public static UserAccount loadAccount(Path file) {
        accountDao = new UserAccountDao.FileBased(file);
        account = accountDao.loadOrCreateDemo();
        return account;
    }
    // Create a new account .json file immediately with just the username and hashed password
    public static void loadNewAccount(Path file, UserAccount newAcc) {
        accountDao = new UserAccountDao.FileBased(file);
        account = newAcc;
        accountDao.save(account);
        if (Files.exists(file)) {
            displayNotif("Account Created", "New Account Created, you can now login.");
        } else {
            displayError("Account Creation Failed", "Something went wrong.");
        }

    }

    // Constructor, which is only used once at the beginning and essentially is our main() starting code.
    public MainWindow() {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // If not in DEMO_MODE, start with the login panel. Should be false on release.
        if (DEMO_MODE) {
            DemoPanel demo = new DemoPanel();
            switchPanel(demo);
        } else {
            LoginPanel login = new LoginPanel();
            switchPanel(login);
        }

        // Run saving method when window is closed.
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveUserAccount();
            }
        });

        window.setVisible(true);
    }
    // Attempts to save the account if that is appropriate.
    public static boolean saveUserAccount() {
        if (panelHistory.isEmpty()) {
            return true; // We are on the login (or Demo) screen, so save already happened or no data was changed.
        } else if (accountDao != null && account != null) { // We have an account we can save.
            accountDao.save(account);
            StringBuilder sb = new StringBuilder();
            sb.append("<html>").append("Your data has been saved to<br>");
            sb.append(accountDao.getFilePath());
            sb.append("</html>");
            MainWindow.displayNotif("Saved", sb.toString());
            return true;
        } else { // Something went wrong, possibly in DEMO_MODE
            MainWindow.displayError("Failed to Save", "Error occurred with saving UserAccount");
            return false;
        }
    }

    // Switches into a new panel, storing the current panel in a stack so we can return to it.
    public static void switchPanel(JPanel newPanel) {
        if (currPanel != null) { // If it was null, we are initializing the first screen.
            panelHistory.push(currPanel); // Push current panel to top of navigation stack
            window.remove(currPanel); // Remove the current panel from our window
            // Note that this doesn't delete it. We are freezing it in time (away from user input).
        }
        currPanel = newPanel;
        window.setContentPane(currPanel); // Set our window to display our new screen.
        window.revalidate(); // Validates all components inside.
        window.pack(); // Shrink or grow our window to fit everything.
        // This line above probably causes our fullscreen issues but no time to fix that.

        if (currPanel instanceof TitledPanel) {
            // If our panel implements TitledPanel, get its title to display it on our window.
            changeTitle(((TitledPanel) currPanel).getTitle());
        } else {
            // Otherwise just reset it to "Perfect Pitch"
            window.setTitle(PROGRAM_TITLE);
        }
        //window.setSize(800, 600);
    }

    // Backtracking to the previous screen with no data being handed off, so null.
    public static void prevPanel() { prevPanel(null); }

    // Backtracking to the previous screen, optionally with sending an object to that screen.
    public static void prevPanel(Object obj) {
        if (currPanel != null) {
            window.remove(currPanel); // Remove our current panel from the window
        }
        currPanel = panelHistory.pop(); // Pop the top of our navigation stack and set that to our current panel
        // Note that we aren't creating a new instance of that panel, we are retrieving it still in memory.
        if (obj != null) { // If we are giving data to the previous panel
            if (currPanel instanceof HostPanel currHost) { // And it is able to accept it
                currHost.receiveObject(obj); // Give it that object
            } else {
                // The previous panel was sent an object but it can't receive it, so a bug happened.
                // OR we are in DEMO_MODE, which stubs out panels.
                // Example: We are saving a randomly generated session that doesn't belong to a pitcher.
                displayError("PROGRAM ERROR","Previous panel given object but does not implement HostPanel");
            }
        }
        window.setContentPane(currPanel);
        window.revalidate();
        window.pack(); // Show this panel, with its components validated and the panel resized for them.
        if (currPanel instanceof TitledPanel) {
            // If our panel implements TitledPanel, get its title to display it on our window.
            changeTitle(((TitledPanel) currPanel).getTitle());
        } else {
            // Otherwise just reset it to "Perfect Pitch"
            window.setTitle(PROGRAM_TITLE);
        }
    }

    // This is a popup panel, which locks the main window to display a new one. Used for forms.
    public static void popupPanel(PopPanel newPanel, String title) {
        popupPanel = newPanel;
        popupWindow =  new JFrame(title);
        popupWindow.add((Component) newPanel);
        popupWindow.pack();
        popupWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        popupWindow.setVisible(true); // Set up our new popup window and display it

        window.setEnabled(false);
        window.setFocusable(false); // Disable and unfocus our main window.
        // Listen for when the popup window is closed.
        popupWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (popupPanel.warnOnClose()) { // If we should warn on close
                    // Display the interface's strings for that warning
                    if (displayOkCancel(popupPanel.getCancelTitle(), popupPanel.getCancelMsg())) {
                        closePopupPanel(); // Close if "OK" is selected
                    } // If "Cancel" or the X button is selected, the popupWindow remains open.
                } else { // The current popupPanel sets warnOnClose to false, so just close it.
                    closePopupPanel();
                }
            }
        });
    }

    // Static method for closing the PopupWindow in the event its canceled (no Obj handoff)
    public static void cancelPopupPanel() {
        popupWindow.dispatchEvent(new WindowEvent(popupWindow, WindowEvent.WINDOW_CLOSING));
    }
    // Close the PopupPanel, ideally with the desire to hand an Obj to the calling Panel
    public static void closePopupPanel() {
        if (popupWindow != null) {
            Object popupObject = popupPanel.sendObject(); // Get the Object the popupPanel created.
            if (currPanel instanceof HostPanel currHost) {
                currHost.receiveObject(popupObject); // Send it to the calling Panel if it can receive it.
            } else {
                // A bug occurred.
                displayError("PROGRAM ERROR","Popup Window created for panel that does not implement HostPanel");
            }
            popupWindow.dispose(); // Close the window, erasing everything from memory.
            window.setEnabled(true);
            window.setFocusable(true);
            window.requestFocus(); // Re-enable the main window and focus it.
        }
    }

    // Display an error message with a red scary icon.
    public static void displayError(String title, String msg) {
        JOptionPane.showMessageDialog(errorWindow, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    // Display a notification with a neutral icon.
    public static void displayNotif(String title, String msg) {
        JOptionPane.showMessageDialog(errorWindow, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Display a popup that can be OKayed or CANCELed, giving back a boolean.
    public static boolean displayOkCancel(String title, String msg) {
        int choice = JOptionPane.showOptionDialog(errorWindow, msg, title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, null, null);
        return choice == 0; // Returns true if "Ok" is selected, and false for anything else.
    }

    // Display a popup with three options, hence an integer result.
    public static int displayYesNoCancel(String title, String msg) {
        int choice = JOptionPane.showOptionDialog(errorWindow, msg, title,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, null, null);
        return switch (choice) {
            case 0 -> 1; // Yes selected, return "1" which could be interpreted as "True"
            case 1 -> 0; // No selected, return "0" which could be interpreted as "False"
            default -> -1; // Otherwise, return "-1" meaning cancel.
        };
    }

    // Change the main window title to "Perfect Pitch — {string}"
    public static void changeTitle(String title) {
        window.setTitle(PROGRAM_TITLE + " — " + title);
    }

    public static void main(String[] args) {
        // Ensure GUI updates are handled on the Event Dispatch Thread (Multiple tutorials recommended this).
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
