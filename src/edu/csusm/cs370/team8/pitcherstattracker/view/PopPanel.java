package edu.csusm.cs370.team8.pitcherstattracker.view;

import javax.accessibility.Accessible;

public interface PopPanel extends Accessible {
    Object sendObject();
    // Should the PopPanel give a warning when closing with the X button? Defaults to true.
    default Boolean warnOnClose() {return true;}
    // Default Title and message to give when above is true. Can be overridden to give more specific text.
    default String getCancelTitle() {return "Are you sure?";}
    default String getCancelMsg() {return "Would you like to discard this?";}
}
