package edu.csusm.cs370.team8.pitcherstattracker.dto;

import java.io.Serializable;

/* DTO for persisting Pitch data.
 */
public class PitchDto implements Serializable {
    private static final long serialVersionUID = 1L;

    public String type;
    public String result;
    public int bases;
    public boolean inZone;
    public double speed;
    public boolean wasSwungAt;
    public int id;
}