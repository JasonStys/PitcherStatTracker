package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;

public class InputPanel extends javax.swing.JPanel {
    public InputPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
        this.setVisible(true);
    }
    //@SuppressWarnings("unchecked")
    private void initComponents() {
        JComboBox typeList = new JComboBox(Pitch.Type.values());
        typeList.setSelectedIndex(1);
        this.add(typeList);
        //typeList.getSelectedIndex();

        JComboBox resultList = new JComboBox(Pitch.Result.values());
        resultList.setSelectedIndex(1);
        this.add(resultList);
        //typeList.getSelectedIndex();

        
    }
}
