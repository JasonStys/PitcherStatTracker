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
    private Object pitchObj = null;

    public InputPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
        this.validate();
        this.setVisible(true);
    }

    @Override
    public Object sendObject() {
        return pitchObj;
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
                "How many bases were scored:"));
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

        JPanel checkboxPane = new JPanel();
        checkboxPane.setLayout(new BoxLayout(checkboxPane, BoxLayout.Y_AXIS));
        //checkboxPane.setBorder(BorderFactory.createTitledBorder("Checkboxes"));
        wasSwungAtCheckbox = new JCheckBox("Did the batter swing at this?");
        wasSwungAtCheckbox.setSelected(true);
        wasSwungAtCheckbox.setHorizontalAlignment(JCheckBox.LEFT);
        //wasSwungAtCheckbox.setToolTipText("baby");
        inZoneCheckbox = new JCheckBox("Was the ball in the zone?");
        inZoneCheckbox.setSelected(true);
        inZoneCheckbox.setHorizontalAlignment(JCheckBox.LEFT);
        checkboxPane.add(wasSwungAtCheckbox);
        checkboxPane.add(inZoneCheckbox);
        //checkboxPane.setPreferredSize(new Dimension(150, 50));
        this.add(checkboxPane);

        submitButton = new JButton("Submit");
        submitButton.setActionCommand("submit");
        submitButton.addActionListener(this::actionPerformed);
        this.add(submitButton);
    }

    public void actionPerformed(ActionEvent e) {
        if ("submit".equals(e.getActionCommand())) {
            submitButton.setEnabled(false);
            Pitch.Type type = Pitch.Type.fromInt(typeList.getSelectedIndex());
            Pitch.Result result = Pitch.Result.fromInt(typeList.getSelectedIndex());
            int bases = (Integer) basesSpinner.getValue();
            boolean inZone = inZoneCheckbox.isSelected();
            double speed = (Double) speedSpinner.getValue();
            boolean wasSwungAt = wasSwungAtCheckbox.isSelected();

            Pitch example = new Pitch(type, result, bases, inZone, speed, wasSwungAt);
            pitchObj = example;
            MainWindow.closePopupPanel();
            //System.out.println(example.toString());
        }
    }
}
