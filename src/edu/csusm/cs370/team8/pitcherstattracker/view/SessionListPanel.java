package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SessionListPanel extends JPanel implements HostPanel, TitledPanel {
    private final Pitcher ourPitcher;

    private final String TITLE = "Pitcher Editor";
    public String getTitle() {return TITLE;}

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
                // Add a new session
                sesListModel.addElement(sesObj);
            } else {
                // Replace the edited session at selection
                sesListModel.setElementAt(sesObj, sessionList.getSelectedIndex());
                isEditingMode = false;
            }
        } // End obj instanceof Pitch
        this.revalidate();
    }

    private void initComponents() {
        // Scrollable list of sessions
        JScrollPane sessionScroller = new JScrollPane();
        sessionScroller.setBorder(BorderFactory.createTitledBorder("Sessions:"));
        sesListModel.addAll(ourPitcher.getSessions());
        sessionList = new JList<>(sesListModel);
        sessionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sessionList.setSelectedIndex(sesListModel.size() - 1);
        sessionList.setLayoutOrientation(JList.VERTICAL);
        sessionList.setVisibleRowCount(-1);
        sessionScroller.setViewportView(sessionList);
        sessionScroller.setPreferredSize(new Dimension(500, 425));
        this.add(sessionScroller, BorderLayout.CENTER); // Add it to the center

        // East sidebar of buttons
        JPanel buttonSidebar = new JPanel();
        buttonSidebar.setLayout(new GridLayout(3, 1));

        newButton = new JButton("Record A New Session", IconGetter.ADD);
        newButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        newButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon
        newButton.setActionCommand("new");
        newButton.addActionListener(this::actionPerformed);

        editButton = new JButton("Edit This Session", IconGetter.EDIT);
        editButton.setActionCommand("edit");
        editButton.addActionListener(this::actionPerformed);
        editButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        editButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon

        deleteButton = new JButton("Delete This Session", IconGetter.CUT);
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(this::actionPerformed);
        deleteButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        deleteButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon

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
        //buttonFooter.setSize(new Dimension(50, 500));
        this.add(buttonFooter, BorderLayout.SOUTH);

        // North Header of button and spinners
        JPanel header = new JPanel();
        //header.setLayout(new GridLayout(1, 4));
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        backButton = new JButton("Back", IconGetter.BACK);
        backButton.setActionCommand("cancel");
        backButton.addActionListener(this::actionPerformed);
        header.add(backButton);

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
                sessionList.setSelectedIndex(-1); // Turn off the selection.
                MainWindow.switchPanel(new SessionEditPanel());
                break;

            case "edit":
                Session editedSession = sessionList.getSelectedValue();
                if (editedSession != null) {
                    isEditingMode = true;
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
                //MainWindow.displayError("PROGRAM ERROR","This button is not assigned to any code!");
                ourPitcher.setName(textField.getText());

                // Turn the current graphical list into a Session list that can be set to the Pitcher.
                ArrayList<Session> sessions;
                sessions = Collections.list(sesListModel.elements());
                ourPitcher.setSessions(sessions);

                MainWindow.prevPanel(ourPitcher);
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
