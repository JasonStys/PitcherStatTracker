package edu.csusm.cs370.team8.pitcherstattracker.view;

import javax.swing.*;
import java.awt.*;

// This class holds static methods for getting common simple "widgets", which are JPanels or JComponents.
// More complicated widgets would get their own classes.
public class WidgetGetter {

    // Glossary for the table panels. It's just labels in 2 columns
    public static JPanel Glossary() {
        JPanel labelPanel = new JPanel();
        labelPanel.setBorder(BorderFactory.createTitledBorder("Glossary:"));

        StringBuilder sbLeft = new StringBuilder();
        sbLeft.append("<html>");
        sbLeft.append("IP = Innings Pitched").append("<br>");
        sbLeft.append("BF = Batters Faced").append("<br>");
        sbLeft.append("P = Pitches").append("<br>");
        sbLeft.append("H = Hits").append("<br>");
        sbLeft.append("BB = Walks (Base on Balls)");
        sbLeft.append("</html>");
        JLabel labelsLeft = new JLabel(sbLeft.toString());
        labelsLeft.setHorizontalAlignment(SwingConstants.LEFT);

        StringBuilder sbRight = new StringBuilder();
        sbRight.append("<html>");
        sbRight.append("K = Strikeouts").append("<br>");
        sbRight.append("AVG = Batting Average Against").append("<br>");
        sbRight.append("OBP = On Base Percentage Against").append("<br>");
        sbRight.append("SLG = Slugging Against").append("<br>");
        sbRight.append("WHIP = Walks + Hits / Innings Pitched");
        sbRight.append("</html>");
        JLabel labelsRight = new JLabel(sbRight.toString());
        labelsRight.setHorizontalAlignment(SwingConstants.LEFT);

        labelPanel.setLayout(new GridLayout(1,2));
        labelPanel.add(labelsLeft);
        labelPanel.add(labelsRight);

        return labelPanel;
    }

    // Button with an icon above it. Not to be used for buttons that don't change Text positions.
    public static JButton makeButton(String label, String actionCommand, ImageIcon icon) {
        JButton newButton = new JButton(label, icon);
        newButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        newButton.setHorizontalTextPosition(AbstractButton.CENTER); // Sets text to be below icon
        newButton.setActionCommand(actionCommand);
        return newButton;
    }
}
