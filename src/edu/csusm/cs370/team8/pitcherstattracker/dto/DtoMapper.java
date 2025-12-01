package edu.csusm.cs370.team8.pitcherstattracker.dto;

import edu.csusm.cs370.team8.pitcherstattracker.model.Pitch;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;
import edu.csusm.cs370.team8.pitcherstattracker.model.Session;
import edu.csusm.cs370.team8.pitcherstattracker.model.UserAccount;

import java.util.ArrayList;
import java.util.List;

/* Utility methods for converting between domain objects and DTOs.
 * Does not modify any existing model classes.
 */
public final class DtoMapper {

    private DtoMapper() { }

    // ===== UserAccount =====

    public static UserAccountDto toDto(UserAccount account) {
        UserAccountDto dto = new UserAccountDto();
        dto.pitchers = new ArrayList<>();

        for (Pitcher p : account.getPitchers()) {
            dto.pitchers.add(toDto(p));
        }
        return dto;
    }

    public static UserAccount fromDto(UserAccountDto dto) {
        UserAccount account = new UserAccount();
        if (dto.pitchers != null) {
            for (PitcherDto pDto : dto.pitchers) {
                account.addPitcher(fromDto(pDto));
            }
        }
        return account;
    }

    // ===== Pitcher =====

    public static PitcherDto toDto(Pitcher pitcher) {
        PitcherDto dto = new PitcherDto();
        dto.name = pitcher.getName();
        dto.sessions = new ArrayList<>();

        for (Session s : pitcher.getSessions()) {
            dto.sessions.add(toDto(s));
        }
        return dto;
    }

    public static Pitcher fromDto(PitcherDto dto) {
        Pitcher pitcher = new Pitcher();
        pitcher.setName(dto.name);

        if (dto.sessions != null) {
            for (SessionDto sDto : dto.sessions) {
                pitcher.addSession(fromDto(sDto));
            }
        }
        return pitcher;
    }

    // ===== Session =====

    public static SessionDto toDto(Session s) {
        SessionDto dto = new SessionDto();
        dto.timestamp = s.getTimestamp();
        dto.pitches = new ArrayList<>();

        for (Pitch p : s.getPitches()) {
            dto.pitches.add(toDto(p));
        }
        return dto;
    }

    public static Session fromDto(SessionDto dto) {
        Session s = new Session();          // use your existing no-arg ctor
        s.setTimestamp(dto.timestamp);
        if (dto.pitches != null) {
            for (PitchDto pDto : dto.pitches) {
                s.addPitch(fromDto(pDto));
            }
        }
        return s;
    }

    // ===== Pitch =====

    public static PitchDto toDto(Pitch p) {
        PitchDto dto = new PitchDto();
        dto.type = p.getType().name();
        dto.result = p.getResult().name();
        dto.bases = p.getBases();
        dto.inZone = p.isInZone();
        dto.speed = p.getSpeed();
        dto.wasSwungAt = p.wasSwungAt();
        return dto;
    }

    public static Pitch fromDto(PitchDto dto) {
        Pitch.Type type = Pitch.Type.valueOf(dto.type);
        Pitch.Result result = Pitch.Result.valueOf(dto.result);
        return new Pitch(type, result, dto.bases, dto.inZone, dto.speed, dto.wasSwungAt);
    }
}