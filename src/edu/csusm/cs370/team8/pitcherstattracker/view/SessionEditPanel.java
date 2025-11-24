package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

public class SessionEditPanel extends JPanel implements HostPanel {
    private final Session ourSession;

    private JList<Pitch> pitchList;
    DefaultListModel<Pitch> pitchListModel = new DefaultListModel<>();
    private JButton editButton;
    private JButton newButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton backButton;

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
        this.setLayout(new BorderLayout(2, 2));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    // ran when InputPanel popup gives back a pitch object to this panel.
    @Override
    public void receiveObject(Object obj) {
        if (obj instanceof Pitch pitchObj) { // If we actually recieved a pitch; if it's null or invalid then nothing happens
            // pitchObj is the newly added Pitch
            int idMatch =  pitchObj.getID();
            if (idMatch == -1) { // If this is a new pitch and not an edit of one already created
                pitchObj.setID(ourSession.getNextID()); // Get the next unused (incremented) ID from session
                pitchListModel.addElement(pitchObj); // Add it at the end
            } else { // We have an ID, so it must be editing a pitch we already have
                if (idMatch == pitchList.getSelectedValue().getID()) {
                    pitchListModel.set(pitchList.getSelectedIndex(), pitchObj);
                } else { // Fallback if for some reason it's not the currently selected item. This ideally shouldn't run.
                    MainWindow.displayError("PROGRAM WARNING","Editing a pitch not currently selected. This is unexpected.");
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
        // Scrollable list of pitches
        JScrollPane pitchScroller = new JScrollPane();
        pitchScroller.setBorder(BorderFactory.createTitledBorder("Pitches:"));
        pitchListModel.addAll(ourSession.getPitches());
        pitchList = new JList<>(pitchListModel);
        pitchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pitchList.setSelectedIndex(0);
        pitchList.setLayoutOrientation(JList.VERTICAL);
        pitchList.setVisibleRowCount(-1);
        pitchScroller.setViewportView(pitchList);
        pitchScroller.setPreferredSize(new Dimension(500, 425));
        this.add(pitchScroller, BorderLayout.CENTER); // Add it to the center

        // East sidebar of buttons
        JPanel buttonSidebar = new JPanel();
        buttonSidebar.setLayout(new GridLayout(3, 1));

        newButton = new JButton("Add A New Pitch", IconGetter.ADD);
        newButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        newButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon
        newButton.setActionCommand("new");
        newButton.addActionListener(this::actionPerformed);

        editButton = new JButton("Edit This Pitch", IconGetter.EDIT);
        editButton.setActionCommand("edit");
        editButton.addActionListener(this::actionPerformed);
        editButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        editButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon

        deleteButton = new JButton("Delete This Pitch", IconGetter.CUT);
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
        saveButton = new JButton("Save All and Exit", IconGetter.SAVE);
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this::actionPerformed);
        cancelButton = new JButton("Discard ALL Changes", IconGetter.DELETE);
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this::actionPerformed);
        buttonFooter.add(saveButton);
        buttonFooter.add(cancelButton);
        //buttonFooter.setSize(new Dimension(50, 500));
        this.add(buttonFooter, BorderLayout.SOUTH);

        // North Header of button
        JPanel buttonHeader = new JPanel();
        //buttonHeader.setLayout(new GridLayout(1, 4));
        buttonHeader.setLayout(new BoxLayout(buttonHeader, BoxLayout.X_AXIS));
        backButton = new JButton("Back", IconGetter.BACK);
        backButton.setActionCommand("cancel");
        backButton.addActionListener(this::actionPerformed);

        buttonHeader.add(backButton);
        this.add(buttonHeader, BorderLayout.NORTH);

    }

    // Each case in the switch statement below is when a button is pressed.
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "new":
                MainWindow.popupPanel(new InputPanel(), "New Pitch");
                break;

            case "edit":
                Pitch editedPitch = pitchList.getSelectedValue();
                if (editedPitch != null) {
                    MainWindow.popupPanel(new InputPanel(editedPitch), "Edit Pitch");
                } else {
                    MainWindow.displayError("No Selection", "Please first select a Pitch to edit");
                }
                break;

            case "delete":
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
                break;

            case "save":
                MainWindow.displayError("Unimplemented","This save button is not assigned to any code!");
                break;

            case "cancel":
                MainWindow.displayError("Unimplemented","The Cancel/Back button is not assigned to any code!");
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR","This button is not assigned to any code!");
        }
    }
}
