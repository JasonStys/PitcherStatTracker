package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InputPanel extends JPanel implements PopPanel {
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

    public InputPanel() {
        setupInputPanel(null);
    }

    public InputPanel(Pitch editedPitch) {
        isEditing = true;
        setupInputPanel(editedPitch);
    }

    private void setupInputPanel(Pitch editedPitch) {
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new GridLayout(4, 2));
        initComponents();
        if (editedPitch != null) {
            PitchID = editedPitch.getID();
            typeList.setSelectedItem(editedPitch.getType());
            resultList.setSelectedItem(editedPitch.getResult());
            basesSpinner.setValue(editedPitch.getBases());
            speedSpinner.setValue(editedPitch.getSpeed());
            wasSwungAtCheckbox.setSelected(editedPitch.wasSwungAt());
            inZoneCheckbox.setSelected(editedPitch.isInZone());
        }
        this.validate();
        this.setVisible(true);
    }

    @Override
    public Object sendObject() {
        return pitchObj;
    }
    public Boolean warnOnClose() {return true;}
    public String getCancelTitle() {
        if (isEditing) {return "Discard this edit?";}
        else {return "Discard this pitch?";}
    }
    public String getCancelMsg() {
        if (isEditing) {return "Would you like to discard this edit?";}
        else {return "Would you like to discard this pitch?";}
    }

    //@SuppressWarnings("unchecked")
    private void initComponents() {
        JPanel typePane = new JPanel();
        //typePane.setPreferredSize(new Dimension(300, 310));
        typePane.setBorder(BorderFactory.createTitledBorder(
                "Pitch Type:"));
        typeList = new JComboBox<>(Pitch.Type.values());
        typeList.setSelectedIndex(1);
        typePane.add(typeList);
        this.add(typePane);

        JPanel resultPane = new JPanel();
        resultPane.setBorder(BorderFactory.createTitledBorder(
                "Result of this pitch:"));
        resultList = new JComboBox<>(Pitch.Result.values());
        resultList.setSelectedIndex(1);
        resultPane.add(resultList);
        this.add(resultPane);

        JPanel basesPane = new JPanel();
        basesPane.setBorder(BorderFactory.createTitledBorder(
                "Number of bases scored:"));
        SpinnerModel baseModel =
                new SpinnerNumberModel(0, //initial value
                        0, //min
                        4, //max
                        1); // steps, integers are 1 step apart
        basesSpinner = new JSpinner(baseModel);
        basesSpinner.setPreferredSize(new Dimension(100, 20));
        basesPane.add(basesSpinner);
        this.add(basesPane);

        JPanel speedPane = new JPanel();
        speedPane.setBorder(BorderFactory.createTitledBorder(
                "Speed of the pitch (MPH):"));
        SpinnerModel speedModel =
                new SpinnerNumberModel(67.067, //initial value
                        0.0, //min
                        130.00, //max
                        1); // steps, integers are 1 step apart
        speedSpinner = new JSpinner(speedModel);
        speedSpinner.setPreferredSize(new Dimension(100, 20));
        speedPane.add(speedSpinner);
        speedPane.setPreferredSize(new Dimension(100, 50));
        this.add(speedPane);

        //JPanel checkboxPane = new JPanel();
        //checkboxPane.setLayout(new BoxLayout(checkboxPane, BoxLayout.Y_AXIS));
        //checkboxPane.setLayout(new GridLayout(2, 1));
        //checkboxPane.setBorder(BorderFactory.createTitledBorder("Checkboxes"));
        wasSwungAtCheckbox = new JCheckBox("Did the batter swing at this?");
        wasSwungAtCheckbox.setSelected(true);
        wasSwungAtCheckbox.setHorizontalAlignment(JCheckBox.LEFT);
        //wasSwungAtCheckbox.setToolTipText("baby");
        inZoneCheckbox = new JCheckBox("Was the ball in the zone?");
        inZoneCheckbox.setSelected(true);
        inZoneCheckbox.setHorizontalAlignment(JCheckBox.LEFT);
        //checkboxPane.add(wasSwungAtCheckbox);
        //checkboxPane.add(inZoneCheckbox);
        //checkboxPane.setPreferredSize(new Dimension(150, 50));
        //this.add(checkboxPane);
        this.add(wasSwungAtCheckbox);
        this.add(inZoneCheckbox);

        submitButton = new JButton("Submit");
        submitButton.setActionCommand("submit");
        submitButton.addActionListener(this::actionPerformed);
        this.add(submitButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this::actionPerformed);
        this.add(cancelButton);
    }

    public void actionPerformed(ActionEvent e) {
        if ("submit".equals(e.getActionCommand())) {
            //submitButton.setEnabled(false);
            Pitch.Type type = Pitch.Type.fromInt(typeList.getSelectedIndex());
            Pitch.Result result = Pitch.Result.fromInt(resultList.getSelectedIndex());
            int bases = (Integer) basesSpinner.getValue();
            boolean inZone = inZoneCheckbox.isSelected();
            double speed = (Double) speedSpinner.getValue();
            boolean wasSwungAt = wasSwungAtCheckbox.isSelected();

            String error = validateInput(result, bases);

            if (error != null)
            {
                MainWindow.displayError("Invalid Input", error);
                return;
            }

            Pitch example = new Pitch(type, result, bases, inZone, speed, wasSwungAt);
            example.setID(PitchID); // Will either be -1 (new) or whatever the ID of the given Pitch in the constructor was.
            pitchObj = example;
            MainWindow.closePopupPanel();
            //System.out.println(example.toString());
        } else if ("cancel".equals(e.getActionCommand())) {
            MainWindow.cancelPopupPanel();
        }
    }

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

        return null; // valid
    }
}


