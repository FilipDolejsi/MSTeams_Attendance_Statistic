package fildol.MSTeamsAttendance;

/** Meeting participant. */
public class Participant {
    private final String fullName;
    private final String joinTime;
    private final String leaveTime;
    private String duration;
    private final String email;
    private final String role;
    private final String id;

    public Participant(String fullName, String joinTime, String leaveTime, String duration, String email, String role, String id){
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

    public String getJoinTime() {
        return joinTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
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
