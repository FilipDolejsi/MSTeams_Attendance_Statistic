package fildol.MSTeamsAttendance;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Meeting meeting = new Meeting(args[0], args[1]);
        meeting.buildParticipant();
        System.out.println(meeting);
    }
}
