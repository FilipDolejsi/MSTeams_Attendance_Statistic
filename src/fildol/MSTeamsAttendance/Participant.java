package fildol.MSTeamsAttendance;

import java.time.LocalDateTime;

/** Meeting participant. */
public class Participant {
    private final String fullName;
    private final LocalDateTime joinTime;
    private final LocalDateTime leaveTime;
    private final Duration duration;
    private final String email;
    private final String role;
    private final String id;

    public Participant(String fullName, LocalDateTime joinTime, LocalDateTime leaveTime, Duration duration, String email, String role, String id){
        this.fullName = fullName;
        this.joinTime = joinTime;
        this.leaveTime = leaveTime;
        this.duration = duration;
        this.email = email;
        this.role = role;
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getId() {
        return id;
    }
}
