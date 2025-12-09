package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.model.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;

// This is the coach dashboard that lists pitchers alphabetically when you first login.
// Always the 2nd screen in our final implementation.
public class PitcherListPanel extends JPanel implements HostPanel, TitledPanel {
    private final UserAccount account;

    private final String TITLE = "Coach Dashboard";
    public String getTitle() {return TITLE;}

    // Component declare.
    private JList<Pitcher> pitcherList;
    DefaultListModel<Pitcher> pitcherListModel = new DefaultListModel<>();
    private JButton editButton;
    private JButton profileButton;
    private JButton addButton;
    private JButton logoutButton;
    private JButton showAllButton;

    // Constructor needs a UserAccount so it can load the pitchers from it.
    public PitcherListPanel(UserAccount a) {
        this.account = a;
        this.setLayout(new BorderLayout(2, 2));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    // Ran when we come back from editing a Pitcher or creating a new one.
    @Override
    public void receiveObject(Object obj) {
        // We received an edited pitcher.
        if (obj instanceof Pitcher pitcherObj) {
            int idMatch =  pitcherObj.getID();
            // See if the pitcher currently selected matches the ID of the one given back.
            if (idMatch == pitcherList.getSelectedValue().getID()) {
                // Update our display list and re-save that pitcher by ID to the account.
                pitcherListModel.set(pitcherList.getSelectedIndex(), pitcherObj);
                account.savePitcher(pitcherObj);
            } else { // Fallback if for some reason it's not the currently selected item. This ideally shouldn't run.
                // I (Devon) haven't ever seen this error appear, but it's a fallback that searches all pitchers in the list.
                MainWindow.displayError("PROGRAM WARNING","Editing a pitcher not currently selected. This is unexpected.");
                for (int i = 0; i < pitcherListModel.size(); i++) {
                    if (pitcherListModel.get(i).getID() == idMatch) { // Search all pitchers for a matching ID
                        pitcherListModel.set(i, pitcherObj); // update our display model
                        account.savePitcher(pitcherObj); // Save it by ID
                        break;
                    }
                } // End for loop. Nothing happens if we received a Pitcher that wasn't in the account's list.
            }
        } // End obj instanceof Pitcher
        else if (obj instanceof String str) { // Given a string that is the name of a new Pitcher
            Pitcher newPitcher = new Pitcher();
            newPitcher.setName(str); // Create a new Pitcher with that name.
            pitcherListModel.addElement(newPitcher); // Add it to our display list.
            account.addPitcher(newPitcher); // Add it to our account.
        }
        this.revalidate(); // Validate the display lists so they are current.
    }

    private void initComponents() {
        // Scrollable list of pitchers
        JScrollPane pitcherScroller = new JScrollPane();
        pitcherScroller.setBorder(BorderFactory.createTitledBorder("Pitchers:"));
        List<Pitcher> allPitchers = account.getPitchers();
        allPitchers.sort(null); // Sorts by name by default.
        pitcherListModel.addAll(allPitchers);
        pitcherList = new JList<>(pitcherListModel);
        pitcherList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only select one at a time.
        pitcherList.setSelectedIndex(pitcherListModel.size() - 1); // Auto select last one.
        pitcherList.setLayoutOrientation(JList.VERTICAL);
        pitcherList.setVisibleRowCount(-1); // Make all rows visible without scrolling.
        pitcherScroller.setViewportView(pitcherList);
        pitcherScroller.setPreferredSize(new Dimension(500, 425));
        this.add(pitcherScroller, BorderLayout.CENTER); // Add it to the center

        // East sidebar of buttons. Each button is self explanatory.
        JPanel buttonSidebar = new JPanel();
        buttonSidebar.setLayout(new GridLayout(3, 1));

        addButton = WidgetGetter.makeButton("Create New Pitcher", "add", IconGetter.ADD);
        addButton.addActionListener(this::actionPerformed);

        editButton = WidgetGetter.makeButton("View/Edit Pitcher Sessions", "edit", IconGetter.EDIT);
        editButton.addActionListener(this::actionPerformed);

        profileButton = WidgetGetter.makeButton("View Yearly Profile", "view", IconGetter.VIEW);
        profileButton.addActionListener(this::actionPerformed);

        // Add the buttons in this order.
        buttonSidebar.add(addButton);
        buttonSidebar.add(editButton);
        buttonSidebar.add(profileButton);
        //buttonSidebar.add(deleteButton); // This should probably exist if we had more time.
        this.add(buttonSidebar, BorderLayout.EAST); // Add it to the East (Right) sidebar

        // North Header of Logout and PitcherTable buttons
        JPanel header = new JPanel();
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
                    // Get the Pitcher session editor from the account object itself
                    MainWindow.switchPanel(new SessionListPanel(account.getPitcher(pitcherList.getSelectedValue().getID())));
                } else {
                    MainWindow.displayError("No Selection", "Please first select a Pitcher to edit");
                }
                break;

            case "showall":
                // This gets the pitchers, sorts them by WHIP, then gives them to the panel for tabling
                List<Pitcher> allPitchers = account.getPitchers();
                allPitchers.sort(Pitcher.WhipComparator); // Sorts by WHIP stat
                MainWindow.switchPanel(new AllPitcherDisplayPanel(allPitchers));
                break;

            case "logout":
                MainWindow.saveUserAccount(); // Force save on logout
                MainWindow.prevPanel();
                break;

            default:
                MainWindow.displayError("PROGRAM ERROR","This button is not assigned to any code!");
        }
    }
}
