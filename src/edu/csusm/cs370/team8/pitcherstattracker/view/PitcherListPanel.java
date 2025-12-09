package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;

public class PitcherListPanel extends JPanel implements HostPanel, TitledPanel {
    private final UserAccount account;

    private final String TITLE = "Coach Dashboard";
    public String getTitle() {return TITLE;}

    private JList<Pitcher> pitcherList;
    DefaultListModel<Pitcher> pitcherListModel = new DefaultListModel<>();
    private JButton editButton;
    private JButton profileButton;
    private JButton addButton;
    private JButton logoutButton;
    private JButton showAllButton;

    public PitcherListPanel(UserAccount a) {
        this.account = a;
        this.setLayout(new BorderLayout(2, 2));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    // Ran when we come back from editing a Pitcher
    @Override
    public void receiveObject(Object obj) {
        if (obj instanceof Pitcher pitcherObj) { // If we actually received a pitcher; if it's null or invalid then nothing happens
            int idMatch =  pitcherObj.getID();
            if (idMatch == pitcherList.getSelectedValue().getID()) {
                pitcherListModel.set(pitcherList.getSelectedIndex(), pitcherObj);
                account.savePitcher(pitcherObj);
            } else { // Fallback if for some reason it's not the currently selected item. This ideally shouldn't run.
                MainWindow.displayError("PROGRAM WARNING","Editing a pitcher not currently selected. This is unexpected.");
                for (int i = 0; i < pitcherListModel.size(); i++) {
                    if (pitcherListModel.get(i).getID() == idMatch) {
                        pitcherListModel.set(i, pitcherObj);
                        account.savePitcher(pitcherObj);
                        break;
                    }
                } // End for loop. Don't currently have anything checking if we somehow edited a pitch not in the list.
            }
        } // End obj instanceof Pitcher
        else if (obj instanceof String str) {
            Pitcher newPitcher = new Pitcher();
            newPitcher.setName(str);
            pitcherListModel.addElement(newPitcher);
            account.addPitcher(newPitcher);
        }
        this.revalidate();
    }

    private void initComponents() {
        // Scrollable list of pitchers
        JScrollPane pitcherScroller = new JScrollPane();
        pitcherScroller.setBorder(BorderFactory.createTitledBorder("Pitchers:"));
        List<Pitcher> allPitchers = account.getPitchers();
        allPitchers.sort(null); // Sorts by name by default.
        pitcherListModel.addAll(allPitchers);
        pitcherList = new JList<>(pitcherListModel);
        pitcherList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pitcherList.setSelectedIndex(pitcherListModel.size() - 1);
        pitcherList.setLayoutOrientation(JList.VERTICAL);
        pitcherList.setVisibleRowCount(-1);
        pitcherScroller.setViewportView(pitcherList);
        pitcherScroller.setPreferredSize(new Dimension(500, 425));
        pitcherList.setSelectedIndex(0);
        this.add(pitcherScroller, BorderLayout.CENTER); // Add it to the center

        // East sidebar of buttons
        JPanel buttonSidebar = new JPanel();
        buttonSidebar.setLayout(new GridLayout(3, 1));

        addButton = new JButton("Create New Pitcher", IconGetter.ADD);
        addButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        addButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon
        addButton.setActionCommand("add");
        addButton.addActionListener(this::actionPerformed);

        editButton = new JButton("View/Edit Pitcher Sessions", IconGetter.EDIT);
        editButton.setActionCommand("edit");
        editButton.addActionListener(this::actionPerformed);
        editButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        editButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon

        profileButton = new JButton("View Yearly Profile", IconGetter.VIEW);
        profileButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        profileButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon
        profileButton.setActionCommand("view");
        profileButton.addActionListener(this::actionPerformed);

        buttonSidebar.add(addButton);
        buttonSidebar.add(editButton);
        buttonSidebar.add(profileButton);
        //buttonSidebar.add(deleteButton);
        this.add(buttonSidebar, BorderLayout.EAST); // Add it to the East (Right) sidebar

        // North Header of button
        JPanel header = new JPanel();
        //header.setLayout(new GridLayout(1, 4));
        //header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setLayout(new GridLayout(1, 2));
        logoutButton = new JButton("Log out", IconGetter.STOP);
        logoutButton.setActionCommand("logout");
        logoutButton.addActionListener(this::actionPerformed);
        showAllButton = new JButton("Overview of ALL Pitchers", IconGetter.WORLD);
        showAllButton.setActionCommand("showall");
        showAllButton.addActionListener(this::actionPerformed);
        showAllButton.setPreferredSize(new Dimension(100, 20));
        header.add(logoutButton);
        header.add(showAllButton);
        this.add(header, BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "add":
                MainWindow.popupPanel(new CreatePitcherPopup(), "Name New Pitcher");
                break;

            case "view":
                if (pitcherList.getSelectedIndex() != -1) {
                    // Get the Pitcher profile from the account object itself
                    MainWindow.switchPanel(new PitcherProfilePanel(account.getPitcher(pitcherList.getSelectedValue().getID())));
                } else {
                    MainWindow.displayError("No Selection", "Please first select a Pitcher to view");
                }
                break;

            case "edit":
                if (pitcherList.getSelectedIndex() != -1) {
                    MainWindow.switchPanel(new SessionListPanel(account.getPitcher(pitcherList.getSelectedValue().getID())));
                } else {
                    MainWindow.displayError("No Selection", "Please first select a Pitcher to edit");
                }
                break;

            case "showall":
                List<Pitcher> allPitchers = account.getPitchers();
                allPitchers.sort(Pitcher.WhipComparator); // Sorts by WHIP stat
                MainWindow.switchPanel(new AllPitcherDisplayPanel(allPitchers));
                break;

            case "logout":
                MainWindow.saveUserAccount();
                MainWindow.prevPanel();
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR","This button is not assigned to any code!");
        }
    }
}
