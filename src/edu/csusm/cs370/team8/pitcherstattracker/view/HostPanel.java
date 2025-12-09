package edu.csusm.cs370.team8.pitcherstattracker.view;

import javax.accessibility.Accessible;

// Implementing this means you can receive data from a child panel, either through forward navigation or a popup.
public interface HostPanel extends Accessible {
    void receiveObject(Object obj);
}
