package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;

public class SessionEditPanel extends JPanel implements HostPanel {
    private final Session ourSession;
    private LocalDate timestamp;
    private JSpinner monthSpinner;
    private JSpinner daySpinner;
    private JSpinner yearSpinner;
    private YearMonth ym;

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
        timestamp = ourSession.getTimestamp();
        ym = YearMonth.from(timestamp);
        
        this.setLayout(new BorderLayout(2, 2));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    // ran when InputPanel popup gives back a pitch object to this panel.
    @Override
    public void receiveObject(Object obj) {
        if (obj instanceof Pitch pitchObj) { // If we actually received a pitch; if it's null or invalid then nothing happens
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

        JPanel monthPane = new JPanel();
        monthPane.setBorder(BorderFactory.createTitledBorder(
                "Month:"));
        SpinnerModel monthModel =
                new SpinnerNumberModel(timestamp.getMonthValue(), //initial value
                        1, //min
                        12, //max
                        1); // steps, integers are 1 step apart
        monthSpinner = new JSpinner(monthModel);
        monthSpinner.setPreferredSize(new Dimension(100, 20));
        // Updates the daySpinner so it accurately shows the num of days in the month/year
        monthSpinner.addChangeListener(e -> {updateDaySpinner();});
        monthPane.add(monthSpinner);
        monthPane.setPreferredSize(new Dimension(100, 50));
        header.add(monthPane);

        JPanel dayPane = new JPanel();
        dayPane.setBorder(BorderFactory.createTitledBorder(
                "Day:"));
        SpinnerModel dayModel =
                new SpinnerNumberModel(timestamp.getDayOfMonth(), //initial value
                        1, //min
                        ym.lengthOfMonth(), //max
                        1); // steps, integers are 1 step apart
        daySpinner = new JSpinner(dayModel);
        daySpinner.setPreferredSize(new Dimension(100, 20));
        dayPane.add(daySpinner);
        dayPane.setPreferredSize(new Dimension(100, 50));
        header.add(dayPane);

        JPanel yearPane = new JPanel();
        yearPane.setBorder(BorderFactory.createTitledBorder(
                "Year:"));
        SpinnerModel yearModel =
                new SpinnerNumberModel(timestamp.getYear(), //initial value
                        1749, //min, year England has first recorded instance of the sport of "Bass-ball"
                        99999, //max
                        1); // steps, integers are 1 step apart
        yearSpinner = new JSpinner(yearModel);
        yearSpinner.setPreferredSize(new Dimension(100, 20));
        // Updates the daySpinner so it accurately shows the num of days in the month/year. This is for leap years!
        yearSpinner.addChangeListener(e -> {updateDaySpinner();});
        // Removes the commas from the thousandth place, so like instead of 2,025 it's 2025
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
        yearPane.add(yearSpinner);
        yearPane.setPreferredSize(new Dimension(100, 50));
        header.add(yearPane);

        this.add(header, BorderLayout.NORTH);
    }

    private void updateDaySpinner() {
        int month = (Integer) monthSpinner.getValue();
        int year = (Integer) yearSpinner.getValue();
        int day = (Integer) daySpinner.getValue();
        ym = YearMonth.of(year, month);

        // If the current day is beyond the length of the month (i.e. Feburary 31st), set to max.
        if (day > ym.lengthOfMonth()) {day = ym.lengthOfMonth();}

        SpinnerModel dayModel =
                new SpinnerNumberModel(day, //initial value
                        1, //min
                        ym.lengthOfMonth(), //max
                        1); // steps, integers are 1 step apart

        daySpinner.setModel(dayModel);
        this.revalidate();
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
                Session newSession = new Session();
                newSession.cloneMetaData(ourSession); // Clones all metadata from the session we were given, may be null.
                for  (int i = 0; i < pitchListModel.getSize(); i++) {
                    newSession.addPitch(pitchListModel.getElementAt(i)); // Add all pitches
                }
                int month = (Integer) monthSpinner.getValue();
                int year = (Integer) yearSpinner.getValue();
                int day = (Integer) daySpinner.getValue();
                timestamp = LocalDate.of(year, month, day);
                newSession.setTimestamp(timestamp); // Set the timestamp of the session

                MainWindow.prevPanel(newSession);
                break;

            case "cancel":
                if (MainWindow.displayOkCancel("Are you sure?", "Are you sure you want to discard everything?")) {
                    MainWindow.prevPanel();
                }
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR","This button is not assigned to any code!");
        }
    }
}
