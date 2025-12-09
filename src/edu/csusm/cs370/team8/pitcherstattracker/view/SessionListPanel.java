package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

// This panel takes a pitcher and displays the sessions for editing, and also to rename the pitcher
public class SessionListPanel extends JPanel implements HostPanel, TitledPanel {
    private final Pitcher ourPitcher;

    private final String TITLE = "Pitcher Editor";
    public String getTitle() {return TITLE;}

    // Components declare
    private JList<Session> sessionList;
    DefaultListModel<Session> sesListModel = new DefaultListModel<>();
    private JButton editButton;
    private JButton newButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton backButton;
    private JTextField textField;


    private boolean isEditingMode = false; // True when we are editing a session, false when adding a new one.

    // Constructor: Takes a pitcher to list its sessions and name
    public SessionListPanel(Pitcher p) {
        this.ourPitcher = p;
        this.setLayout(new BorderLayout(2, 2));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    // Ran when we come back from either adding or editing a session.
    @Override
    public void receiveObject(Object obj) {
        if (obj instanceof Session sesObj) { // If we actually received a session; if it's null or invalid then nothing happens
            if ((sessionList.getSelectedIndex() == -1) || !isEditingMode) {
                // Add a new session if no sessions is selected and we are not in editing mode
                sesListModel.addElement(sesObj);
            } else {
                // Replace the edited session at selection.
                // Will error with nothing is selected but haven't had an instance of that ever happening.
                sesListModel.setElementAt(sesObj, sessionList.getSelectedIndex());
                isEditingMode = false; // Set editing mode back to false, the default.
            }
        } // End obj instanceof Pitch
        this.revalidate();
    }

    private void initComponents() {
        // Scrollable list of sessions
        JScrollPane sessionScroller = new JScrollPane();
        sessionScroller.setBorder(BorderFactory.createTitledBorder("Sessions:"));
        sesListModel.addAll(ourPitcher.getSessions()); // By default, sessions are sorted by timestamp.
        sessionList = new JList<>(sesListModel);
        sessionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sessionList.setSelectedIndex(sesListModel.size() - 1); // Start with last session being selected
        sessionList.setLayoutOrientation(JList.VERTICAL);
        sessionList.setVisibleRowCount(-1);
        sessionScroller.setViewportView(sessionList);
        sessionScroller.setPreferredSize(new Dimension(500, 425));
        this.add(sessionScroller, BorderLayout.CENTER); // Add it to the center

        // East sidebar of buttons
        JPanel buttonSidebar = new JPanel();
        buttonSidebar.setLayout(new GridLayout(3, 1));

        newButton = WidgetGetter.makeButton("Record A New Session", "new", IconGetter.ADD);
        newButton.addActionListener(this::actionPerformed);

        editButton = WidgetGetter.makeButton("Edit This Session", "edit", IconGetter.EDIT);
        editButton.addActionListener(this::actionPerformed);

        deleteButton = WidgetGetter.makeButton("Delete This Session", "delete", IconGetter.CUT);
        deleteButton.addActionListener(this::actionPerformed);

        // Add the buttons in this order.
        buttonSidebar.add(newButton);
        buttonSidebar.add(editButton);
        buttonSidebar.add(deleteButton);
        this.add(buttonSidebar, BorderLayout.EAST); // Add it to the East (Right) sidebar

        // South Footer of buttons
        JPanel buttonFooter = new JPanel();
        buttonFooter.setLayout(new GridLayout(1, 2));
        saveButton = new JButton("Save All and Return", IconGetter.SAVE);
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this::actionPerformed);
        cancelButton = new JButton("Discard ALL Changes", IconGetter.DELETE);
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this::actionPerformed);
        buttonFooter.add(saveButton);
        buttonFooter.add(cancelButton);
        this.add(buttonFooter, BorderLayout.SOUTH);

        // North Header of button and name input
        JPanel header = new JPanel();
        //header.setLayout(new GridLayout(1, 4));
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        backButton = new JButton("Back", IconGetter.BACK);
        backButton.setActionCommand("cancel");
        backButton.addActionListener(this::actionPerformed);
        header.add(backButton);

        // Input or the name of the pitcher
        JPanel namePane = new JPanel();
        namePane.setBorder(BorderFactory.createTitledBorder(
                "Name:"));
        textField = new JTextField(50);
        textField.setText(ourPitcher.getName());
        namePane.add(textField);
        header.add(namePane);

        this.add(header, BorderLayout.NORTH);
    }

    // Each case in the switch statement below is when a button is pressed.
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "new":
                isEditingMode = false; // Defaults to false but just in case.
                sessionList.setSelectedIndex(-1); // Turn off the selection.
                MainWindow.switchPanel(new SessionEditPanel());
                break;

            case "edit":
                Session editedSession = sessionList.getSelectedValue();
                if (editedSession != null) { // Check if we have a selected session
                    isEditingMode = true; // Note we are coming back with an edited session after this panel is opened.
                    MainWindow.switchPanel(new SessionEditPanel(editedSession));
                } else {
                    MainWindow.displayError("No Selection", "Please first select a Session to edit");
                }
                break;

            case "delete":
                int removeIndex = sessionList.getSelectedIndex();
                if (removeIndex >= 0) {
                    if (MainWindow.displayOkCancel("Are you sure?", "Are you sure you want to delete this entire Session?")) {
                        sesListModel.remove(removeIndex);
                    }
                } else {
                    MainWindow.displayError("No Selection", "Please first select a Session to delete");
                }
                break;

            case "save":
                if (textField.getText().isEmpty()) { // Make sure we aren't setting the name to be an empty string.
                    MainWindow.displayError("No Name Given","You can't save this pitcher without a name!");
                    return;
                } else {
                    ourPitcher.setName(textField.getText());
                }

                // Turn the current graphical list into a Session list that can be set to the Pitcher.
                ArrayList<Session> sessions;
                sessions = Collections.list(sesListModel.elements());
                ourPitcher.setSessions(sessions); // Set the sessions of our pitcher to our final graphical list.

                MainWindow.prevPanel(ourPitcher); // Return the Pitcher back to the previous panel.
                break;

            case "cancel":
                if (MainWindow.displayOkCancel("Are you sure?", "Are you sure you want to discard all changes?")) {
                    MainWindow.prevPanel();
                }
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR","This button is not assigned to any code!");
        }
    }
}
