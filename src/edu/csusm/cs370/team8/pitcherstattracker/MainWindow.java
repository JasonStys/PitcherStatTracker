package edu.csusm.cs370.team8.pitcherstattracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import edu.csusm.cs370.team8.pitcherstattracker.model.*;
import edu.csusm.cs370.team8.pitcherstattracker.view.*;

/* This might just be more program startup stuff and we'll have a separate
 * "GraphicInterface" class that handles the window that the user interacts with.
 */

public class MainWindow {
    private static JFrame window = new JFrame("Perfect Pitch");
    private static JPanel currPanel; // Current menu in the window
    private static JPanel prevPanel; // Stores previous menu in case user backtracks

    private static JFrame popupWindow;
    private static PopPanel popupPanel;

    public MainWindow() {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Session exampleSession = createExampleSessionData();
        //InputPanel input = new InputPanel();
        SessionEditPanel sesEdit = new SessionEditPanel(exampleSession);
        switchPanel(sesEdit);

        window.setVisible(true);
    }

    public static void switchPanel(JPanel newPanel) {
        prevPanel = currPanel;
        if (currPanel != null) {
            window.remove(currPanel);
        }
        currPanel = newPanel;
        window.setContentPane(newPanel);
        window.pack();
        //window.setSize(800, 600);
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
                closePopupPanel();
            }
        });
    }
    public static void closePopupPanel() {
        if (popupWindow != null) {
            Object popupObject = popupPanel.sendObject();
            if (currPanel instanceof HostPanel currHost) {
                currHost.receiveObject(popupObject);
            } else {
                System.out.println("Popup Window created for panel that does not implement HostPanel");
            }
            popupWindow.dispose(); // Close the window
            window.setEnabled(true);
            window.setFocusable(true);
            window.requestFocus();
        }
    }

    public static void main(String[] args) {
        // Ensure GUI updates are handled on the Event Dispatch Thread
        SwingUtilities.invokeLater(MainWindow::new);
    }

    private static Session createExampleSessionData() {
        Session exampleSession = new Session();
        for (int i = 0; i < 10; i++) {
            exampleSession.addPitch(Pitch.randomPitch());
        }
        return exampleSession;
    }
}
