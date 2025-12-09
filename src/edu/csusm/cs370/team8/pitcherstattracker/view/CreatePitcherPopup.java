package edu.csusm.cs370.team8.pitcherstattracker.view;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// This popup is for creating a new pitcher. Right now only a single text field for the name.
public class CreatePitcherPopup extends JPanel implements PopPanel {
    private JTextField textField;
    private JButton submitButton;
    private JButton cancelButton;

    // Field
    String name = null;

    // Constructor doesn't need anything
    public CreatePitcherPopup() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
        this.revalidate();
        this.setVisible(true);
    }

    private void initComponents() {
        // Everything in a single panel to manage its size
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Text input with label
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        JLabel usernameLabel = new JLabel("Pitcher Name: ");
        textField = new JTextField(30);
        textPanel.add(usernameLabel);
        textPanel.add(textField);
        textPanel.setPreferredSize(new Dimension(50,30));

        // Submit and Cancel buttons.
        JPanel buttonPanel = new JPanel(new  FlowLayout(FlowLayout.RIGHT));
        submitButton = new JButton("Submit");
        submitButton.setActionCommand("submit");
        submitButton.addActionListener(this::actionPerformed);
        buttonPanel.add(submitButton);
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this::actionPerformed);
        buttonPanel.add(cancelButton);

        // Add and lock size of the popup text fields.
        centerPanel.add(textPanel);
        centerPanel.setPreferredSize(new Dimension(300, 30));
        centerPanel.setMinimumSize(new Dimension(300, 30));
        centerPanel.setMaximumSize(new Dimension(300, 30));
        this.add(centerPanel);
        this.add(buttonPanel);
    }

    public void actionPerformed(ActionEvent e) {
        if ("submit".equals(e.getActionCommand())) {
            if (!textField.getText().isEmpty()) {
                name = textField.getText(); // Only when we submit with non-null does it set the name field.
                MainWindow.closePopupPanel();
            } else {
                MainWindow.displayError("Empty Input", "Please enter a name or cancel.");
            }

        } else if ("cancel".equals(e.getActionCommand())) {
            MainWindow.cancelPopupPanel(); // Will give warning, and since name = null it won't use that String.
        }
    }

        @Override
        public Object sendObject() {
            return name; // null or set if submit was executed. If null the calling panel does nothing with it.
        }
}
