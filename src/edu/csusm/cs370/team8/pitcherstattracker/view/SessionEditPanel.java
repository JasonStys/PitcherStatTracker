package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SessionEditPanel extends JPanel implements HostPanel {
    private Session ourSession;

    private JList<Pitch> pitchList;
    DefaultListModel<Pitch> pitchListModel = new DefaultListModel<>();
    private JButton editButton;
    private JButton newButton;
    private JButton deleteButton;

    // Empty constructor, we are creating a new Session
    public SessionEditPanel() {
        this.ourSession = new Session();
        setupSessionPanel();
    }

    // Constructor with session, we are modifying a session further.
    public SessionEditPanel(Session s) {
        this.ourSession = s;
        setupSessionPanel();
    }

    private void setupSessionPanel() {
        this.setLayout(new BorderLayout());
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    // ran when InputPanel popup gives back a pitch object to this panel.
    @Override
    public void receiveObject(Object obj) {
        if (obj instanceof Pitch pitchObj) {
            // pitchObj is the newly added Pitch
            int idMatch =  pitchObj.getID();
            if (idMatch == -1) { // If this is a new pitch and not an edit of one already created
                pitchObj.setID(ourSession.getNextID()); // Get the next unused (incremented) ID from session
                pitchListModel.addElement(pitchObj); // Add it at the end
            } else { // We have an ID, so it must be editing a pitch we already have
                if (idMatch == pitchList.getSelectedValue().getID()) {
                    pitchListModel.set(pitchList.getSelectedIndex(), pitchObj);
                } else { // Fallback if for some reason it's not the currently selected item. This ideally shouldn't run.
                    MainWindow.displayError("PROGRAM ERROR","This shouldn't run.");
                    for (int i = 0; i < pitchListModel.size(); i++) {
                        if (pitchListModel.get(i).getID() == idMatch) {
                            pitchListModel.set(i, pitchObj);
                            break;
                        }
                    } // End for loop. Don't currently have anything checking if we somehow edited a pitch not in the list.
                }
            } // End id matching
        } // End obj instanceof Pitch
        this.revalidate();
    }

    private void initComponents() {
        JScrollPane pitchScroller = new JScrollPane();
        pitchScroller.setBorder(BorderFactory.createTitledBorder("Pitches:"));
        //DefaultListModel<Pitch> pitchListModel = new DefaultListModel<>();
        pitchListModel.addAll(ourSession.getPitches());
        pitchList = new JList<>(pitchListModel);
        pitchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pitchList.setSelectedIndex(0);
        pitchList.setLayoutOrientation(JList.VERTICAL);
        pitchList.setVisibleRowCount(-1);
        //pitchList.setPreferredSize(new Dimension(400, 400));
        pitchScroller.setViewportView(pitchList);
        pitchScroller.setPreferredSize(new Dimension(425, 425));
        this.add(pitchScroller, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(3, 1));

        newButton = new JButton("Add A New Pitch");
        newButton.setActionCommand("new");
        newButton.addActionListener(this::actionPerformed);
        //newButton.setPreferredSize(new Dimension(100, 50));

        editButton = new JButton("Edit This Pitch");
        editButton.setActionCommand("edit");
        editButton.addActionListener(this::actionPerformed);
        deleteButton = new JButton("Delete This Pitch");
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(this::actionPerformed);

        buttonPane.add(newButton);
        buttonPane.add(editButton);
        buttonPane.add(deleteButton);
        this.add(buttonPane, BorderLayout.EAST);
    }

    public void actionPerformed(ActionEvent e) {
        if ("edit".equals(e.getActionCommand())) {
            Pitch editedPitch = pitchList.getSelectedValue();
            if (editedPitch != null) {
                MainWindow.popupPanel(new InputPanel(editedPitch), "Edit Pitch");
            } else {
                MainWindow.displayError("No Selection", "Please first select a Pitch to edit");
            }
        }
        if ("new".equals(e.getActionCommand())) {
            MainWindow.popupPanel(new InputPanel(), "New Pitch");
        }
        if ("delete".equals(e.getActionCommand())) {
            int removeIndex = pitchList.getSelectedIndex();
            if (removeIndex >= 0) {
                if (MainWindow.displayOkCancel("Are you sure?", "Are you sure you want to delete this pitch?")) {
                    pitchListModel.remove(removeIndex);
                }
            } else {
                MainWindow.displayError("No Selection", "Please first select a Pitch to delete");
                // Testing displayTestNoCancel
                //System.out.println(MainWindow.displayYesNoCancel("Title", "Message"));
            }
        }
    }
}
