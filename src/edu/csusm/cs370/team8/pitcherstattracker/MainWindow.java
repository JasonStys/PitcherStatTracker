package edu.csusm.cs370.team8.pitcherstattracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayDeque;
import java.util.Deque;

import java.nio.file.Path;
import java.nio.file.Paths;

import edu.csusm.cs370.team8.pitcherstattracker.dao.UserAccountDao;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;
import edu.csusm.cs370.team8.pitcherstattracker.view.*;

/* This might just be more program startup stuff and we'll have a separate
 * "GraphicInterface" class that handles the window that the user interacts with.
 */

public class MainWindow {
    private static final String PROGRAM_TITLE = "Perfect Pitch";
    private static JFrame window = new JFrame(PROGRAM_TITLE);
    private static JPanel currPanel; // Current menu in the window
    private static Deque<JPanel> panelHistory = new ArrayDeque<>(); // Used as a stack, with .push() and .pop() methods.

    private static JFrame popupWindow;
    private static PopPanel popupPanel;

    private static JFrame errorWindow = new JFrame(); // Does not need any further setup as JOptionPanel handles it.
    // errorWindow is also used for confirmation popup windows.

    // DAO / persistence fields
    private static UserAccountDao accountDao;
    private static UserAccount account;
    public static UserAccount getUser() {return account;}

    public MainWindow() {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // DAO + account setup
        Path dataFile = Paths.get("data","account.json");
        accountDao = new UserAccountDao.FileBased(dataFile);
        account = accountDao.loadOrCreateDemo();

        DemoPanel demo = new DemoPanel();
        switchPanel(demo);

        // save on close
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (accountDao != null && account != null) {
                    accountDao.save(account);
                }
            }
        });

        window.setVisible(true);
    }

    public static void switchPanel(JPanel newPanel) {
        if (currPanel != null) {
            panelHistory.push(currPanel);
            window.remove(currPanel);
        }
        currPanel = newPanel;
        window.setContentPane(currPanel);
        window.revalidate();
        window.pack();
        if (currPanel instanceof TitledPanel) {
            changeTitle(((TitledPanel) currPanel).getTitle());
        } else {
            window.setTitle(PROGRAM_TITLE);
        }
        //window.setSize(800, 600);
    }

    public static void prevPanel() { prevPanel(null); }
    public static void prevPanel(Object obj) {
        if (currPanel != null) {
            window.remove(currPanel);
        }
        currPanel = panelHistory.pop();
        if (obj != null) {
            if (currPanel instanceof HostPanel currHost) {
                currHost.receiveObject(obj);
            } else {
                displayError("PROGRAM ERROR","Previous panel given object but does not implement HostPanel");
            }
        }
        window.setContentPane(currPanel);
        window.revalidate();
        window.pack();
        if (currPanel instanceof TitledPanel) {
            changeTitle(((TitledPanel) currPanel).getTitle());
        } else {
            window.setTitle(PROGRAM_TITLE);
        }
    }

    public static void popupPanel(PopPanel newPanel, String title) {
        popupPanel = newPanel;
        popupWindow =  new JFrame(title);
        popupWindow.add((Component) newPanel);
        popupWindow.pack();
        popupWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        popupWindow.setVisible(true);

        window.setEnabled(false);
        window.setFocusable(false);
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
    public static void cancelPopupPanel() {
        popupWindow.dispatchEvent(new WindowEvent(popupWindow, WindowEvent.WINDOW_CLOSING));
    }
    public static void closePopupPanel() {
        if (popupWindow != null) {
            Object popupObject = popupPanel.sendObject();
            if (currPanel instanceof HostPanel currHost) {
                currHost.receiveObject(popupObject);
            } else {
                displayError("PROGRAM ERROR","Popup Window created for panel that does not implement HostPanel");
            }
            popupWindow.dispose(); // Close the window
            window.setEnabled(true);
            window.setFocusable(true);
            window.requestFocus();
        }
    }

    public static void displayError(String title, String msg) {
        JOptionPane.showMessageDialog(errorWindow, msg, title, JOptionPane.ERROR_MESSAGE);
    }
    public static boolean displayOkCancel(String title, String msg) {
        int choice = JOptionPane.showOptionDialog(errorWindow, msg, title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, null, null);
        return choice == 0; // Returns true if "Ok" is selected, and false for anything else.
    }
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

    public static void changeTitle(String title) {
        window.setTitle(PROGRAM_TITLE + " — " + title);
    }

    public static void main(String[] args) {
        // Ensure GUI updates are handled on the Event Dispatch Thread
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
