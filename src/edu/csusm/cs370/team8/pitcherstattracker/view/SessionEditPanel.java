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

    @Override
    public void receiveObject(Object obj) {
        if (obj instanceof Pitch pitchObj) {
            // pitchObj is the newly added Pitch
            pitchListModel.addElement(pitchObj);
        }
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
            System.out.println("edit");
        }
        if ("new".equals(e.getActionCommand())) {
            System.out.println("new");
            MainWindow.popupPanel(new InputPanel(), "New Pitch");
        }
        if ("delete".equals(e.getActionCommand())) {
            System.out.println("delete");
        }
    }
}
