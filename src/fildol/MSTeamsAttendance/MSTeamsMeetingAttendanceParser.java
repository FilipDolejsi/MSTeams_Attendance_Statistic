package fildol.MSTeamsAttendance;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MSTeamsMeetingAttendanceParser {
    public MSTeamsMeetingAttendanceParser() {
    }

    public int timeToInt(int[] placeholder) {
        int daysToHours = placeholder[0] * 24;
        return ((daysToHours + placeholder[1]) * 60) + placeholder[2];
    }

    public String formatMeetingDuration(String startTime, String endTime) {
        Duration meetingDur;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("h:m:s a");

        LocalTime time1 = LocalTime.parse(startTime, format);
        LocalTime time2 = LocalTime.parse(endTime, format);

        meetingDur = Duration.between(time1, time2);

        String meetingDuration = (meetingDur.toMinutes() + " minutes " + meetingDur.toSecondsPart() + " seconds");
        return meetingDuration;
    }

}
