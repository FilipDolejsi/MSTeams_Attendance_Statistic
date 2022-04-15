package fildol.MSTeamsAttendance;

public class Participant {
    private String fullName;
    private String joinTime;
    private String leaveTime;
    private String duration;
    private String email;
    private String role;
    private String id;

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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
