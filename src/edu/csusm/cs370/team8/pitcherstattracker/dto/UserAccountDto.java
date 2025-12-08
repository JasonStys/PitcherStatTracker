package edu.csusm.cs370.team8.pitcherstattracker.dto;

import java.io.Serializable;
import java.util.List;

/* DTO for persisting UserAccount data.
 */
public class UserAccountDto implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<PitcherDto> pitchers;
}
