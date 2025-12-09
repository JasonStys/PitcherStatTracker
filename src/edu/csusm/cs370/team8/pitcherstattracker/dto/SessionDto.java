package edu.csusm.cs370.team8.pitcherstattracker.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/* DTO for persisting Session data.
 */
public class SessionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    public int sessionId;
    public LocalDate timestamp;
    public List<PitchDto> pitches;
    public int nextPitchID;
}