package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// This is the form for inputting a new Pitch.
public class PitchInputPopup extends JPanel implements PopPanel {
    private JComboBox<Pitch.Type> typeList;
    private JComboBox<Pitch.Result> resultList;
    private JSpinner basesSpinner;
    private JSpinner speedSpinner;
    private JCheckBox wasSwungAtCheckbox;
    private JCheckBox inZoneCheckbox;
    private JButton submitButton;
    private JButton cancelButton;
    private Object pitchObj = null;
    private int PitchID = -1; // Defaults to negative 1 unless assigned
    private boolean isEditing = false;

    // Empty constructor, Creating a new pitch
    public PitchInputPopup() {
        setupInputPanel(null);
    }

    // Constructor when given a Pitch, setting editing mode
    public PitchInputPopup(Pitch editedPitch) {
        isEditing = true;
        setupInputPanel(editedPitch);
    }

    private void setupInputPanel(Pitch editedPitch) {
        this.setLayout(new GridLayout(4, 2));
        initComponents(); // Create the components.
        // Swap all the defaults for values from the Pitch if we're editing.
        if (editedPitch != null) {
            PitchID = editedPitch.getID();
            typeList.setSelectedItem(editedPitch.getType());
            resultList.setSelectedItem(editedPitch.getResult());
            basesSpinner.setValue(editedPitch.getBases());
            speedSpinner.setValue(editedPitch.getSpeed());
            wasSwungAtCheckbox.setSelected(editedPitch.wasSwungAt());
            inZoneCheckbox.setSelected(editedPitch.isInZone());
        }
        this.validate(); // Validate all components are current
        this.setVisible(true);
    }

    // send an Object (in this case a Pitch) back to the previous screen when requested.
    @Override
    public Object sendObject() {
        return pitchObj;
    }

    public Boolean warnOnClose() {return true;}
    // Title of popup when we're canceling
    public String getCancelTitle() {
        if (isEditing) {return "Discard this edit?";}
        else {return "Discard this pitch?";}
    }
    // Message of popup when we're canceling
    public String getCancelMsg() {
        if (isEditing) {return "Would you like to discard this edit?";}
        else {return "Would you like to discard this pitch?";}
    }

    private void initComponents() {
        // Dropdown for the Type
        JPanel typePane = new JPanel();
        typePane.setBorder(BorderFactory.createTitledBorder(
                "Pitch Type:"));
        typeList = new JComboBox<>(Pitch.Type.values());
        typeList.setSelectedIndex(1);
        typePane.add(typeList);
        this.add(typePane);

        // Dropdown for the result
        JPanel resultPane = new JPanel();
        resultPane.setBorder(BorderFactory.createTitledBorder(
                "Result of this pitch:"));
        resultList = new JComboBox<>(Pitch.Result.values());
        resultList.setSelectedIndex(1);
        resultPane.add(resultList);
        this.add(resultPane);

        // Spinner for the bases
        JPanel basesPane = new JPanel();
        basesPane.setBorder(BorderFactory.createTitledBorder(
                "Number of bases scored:"));
        SpinnerModel baseModel =
                new SpinnerNumberModel(0, //initial value
                        Pitch.MIN_BASES, //min
                        Pitch.MAX_BASES, //max
                        1); // steps, integers are 1 step apart
        basesSpinner = new JSpinner(baseModel);
        basesSpinner.setPreferredSize(new Dimension(100, 20));
        basesPane.add(basesSpinner);
        this.add(basesPane);

        // Spinner for the speed. Could maybe be abstracted but not a lot of consistent instances of it.
        // Also for validation we would need to keep some stuff in this class.
        JPanel speedPane = new JPanel();
        speedPane.setBorder(BorderFactory.createTitledBorder(
                "Speed of the pitch (MPH):"));
        SpinnerModel speedModel =
                new SpinnerNumberModel(60.000, //initial value
                        Pitch.MIN_SPEED, //min
                        Pitch.MAX_SPEED, //max
                        1); // steps, integers are 1 step apart
        speedSpinner = new JSpinner(speedModel);
        speedSpinner.setPreferredSize(new Dimension(100, 20));
        speedPane.add(speedSpinner);
        speedPane.setPreferredSize(new Dimension(100, 50));
        this.add(speedPane);

        // Checkboxes.
        wasSwungAtCheckbox = new JCheckBox("Did the batter swing at this?");
        wasSwungAtCheckbox.setSelected(true);
        wasSwungAtCheckbox.setHorizontalAlignment(JCheckBox.LEFT);
        inZoneCheckbox = new JCheckBox("Was the ball in the zone?");
        inZoneCheckbox.setSelected(true);
        inZoneCheckbox.setHorizontalAlignment(JCheckBox.LEFT);
        this.add(wasSwungAtCheckbox);
        this.add(inZoneCheckbox);

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.setActionCommand("submit");
        submitButton.addActionListener(this::actionPerformed);
        this.add(submitButton);

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this::actionPerformed);
        this.add(cancelButton);
    }

    public void actionPerformed(ActionEvent e) {
        if ("submit".equals(e.getActionCommand())) {
            // Get the values from our input JComponents
            Pitch.Type type = Pitch.Type.fromInt(typeList.getSelectedIndex());
            Pitch.Result result = Pitch.Result.fromInt(resultList.getSelectedIndex());
            int bases = (Integer) basesSpinner.getValue();
            boolean inZone = inZoneCheckbox.isSelected();
            double speed = (Double) speedSpinner.getValue();
            boolean wasSwungAt = wasSwungAtCheckbox.isSelected();

            // Get an error string if there is a validation error
            String error = validateInput(result, bases);

            if (error != null)
            {
                // Print the error message and cancel the saving.
                MainWindow.displayError("Invalid Input", error);
                return;
            }

            // Create the Pitch
            Pitch example = new Pitch(type, result, bases, inZone, speed, wasSwungAt);
            example.setID(PitchID); // Will either be -1 (new) or whatever the ID of the given Pitch in the constructor was.
            pitchObj = example; // Set this to the object we'll send back to the previous panel
            MainWindow.closePopupPanel();
            //System.out.println(example.toString());
        } else if ("cancel".equals(e.getActionCommand())) {
            MainWindow.cancelPopupPanel(); // Cancel with a null pitchObj, so nothing is done.
        }
    }

    // This could be expanded or made into a controller class.
    private String validateInput(Pitch.Result result, int bases) {
        // Non-contact results → bases must be 0
        switch (result) {
            case Ball:
            case Strike:
            case Foul:
            case Strikeout:
            case BallInPlayOut:
            case HitByPitch:
            case Walk:
                if (bases != 0) {
                    return "This pitch result cannot have bases gained.";
                }
                break;

            // Hit or error → bases must be 1–4
            case Hit:
            case ReachOnError:
                if (bases < 1 || bases > 4) {
                    return "Hits or errors must result in 1–4 bases.";
                }
                break;
        }

        return null; // valid input, so no string is produced.
    }
}


