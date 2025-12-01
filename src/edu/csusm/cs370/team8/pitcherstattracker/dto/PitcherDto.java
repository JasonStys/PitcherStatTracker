package edu.csusm.cs370.team8.pitcherstattracker.dto;

import java.io.Serializable;
import java.util.List;

/* DTO for persisting Pitcher data.
 */
public class PitcherDto implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String name;
    public List<SessionDto> sessions;
}