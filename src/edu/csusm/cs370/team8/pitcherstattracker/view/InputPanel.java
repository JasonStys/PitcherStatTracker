package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends javax.swing.JPanel {
    public InputPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
        this.setVisible(true);
    }
    //@SuppressWarnings("unchecked")
    private void initComponents() {
        JPanel typePane = new JPanel();
        //typePane.setPreferredSize(new Dimension(300, 310));
        typePane.setBorder(BorderFactory.createTitledBorder(
                "Pitch Type:"));
        JComboBox typeList = new JComboBox(Pitch.Type.values());
        typeList.setSelectedIndex(1);
        typePane.add(typeList);
        this.add(typePane);
        //typeList.getSelectedIndex();

        JPanel resultPane = new JPanel();
        resultPane.setBorder(BorderFactory.createTitledBorder(
                "Result of this pitch:"));
        JComboBox resultList = new JComboBox(Pitch.Result.values());
        resultList.setSelectedIndex(1);
        resultPane.add(resultList);
        this.add(resultPane);
        //Pitch.Result result = Pitch.Result.fromInt(typeList.getSelectedIndex());

        JPanel basesPane = new JPanel();
        basesPane.setBorder(BorderFactory.createTitledBorder(
                "How many bases were scored:"));
        SpinnerModel baseModel =
                new SpinnerNumberModel(0, //initial value
                        0, //min
                        4, //max
                        1); // steps, integers are 1 step apart
        JSpinner basesSpinner = new JSpinner(baseModel);
        basesSpinner.setPreferredSize(new Dimension(100, 20));
        basesPane.add(basesSpinner);
        this.add(basesPane);
        //int bases = (Integer) basesSpinner.getValue();

        JPanel speedPane = new JPanel();
        speedPane.setBorder(BorderFactory.createTitledBorder(
                "Speed of the pitch:"));
        SpinnerModel speedModel =
                new SpinnerNumberModel(67.067, //initial value
                        0.0, //min
                        130.00, //max
                        1); // steps, integers are 1 step apart
        JSpinner speedSpinner = new JSpinner(speedModel);
        speedSpinner.setPreferredSize(new Dimension(100, 20));
        speedPane.add(speedSpinner);
        speedPane.setPreferredSize(new Dimension(100, 50));
        this.add(speedPane);
        //double speed = (Double) speedSpinner.getValue();

        JPanel checkboxPane = new JPanel();
        checkboxPane.setLayout(new BoxLayout(checkboxPane, BoxLayout.Y_AXIS));
        //checkboxPane.setBorder(BorderFactory.createTitledBorder("Checkboxes"));
        JCheckBox wasSwungAtCheckbox = new JCheckBox("Did the batter swing at this?");
        wasSwungAtCheckbox.setSelected(true);
        wasSwungAtCheckbox.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JCheckBox inZoneCheckbox = new JCheckBox("Was the ball in the zone?");
        inZoneCheckbox.setSelected(true);
        inZoneCheckbox.setAlignmentX(Component.RIGHT_ALIGNMENT);
        checkboxPane.add(wasSwungAtCheckbox);
        checkboxPane.add(inZoneCheckbox);
        //checkboxPane.setPreferredSize(new Dimension(150, 50));
        this.add(checkboxPane);
        //boolean wasSwungAt = wasSwungAtCheckbox.isSelected();
        //boolean inZone = inZoneCheckbox.isSelected();
    }
}
