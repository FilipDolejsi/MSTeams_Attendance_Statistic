package fildol.MSTeamsAttendance;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeParser {

  public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("M/d/y, h:m:s a");

  public DateTimeParser() {}

  public static fildol.MSTeamsAttendance.Duration computeMeetingDuration(
      LocalDateTime startTime, LocalDateTime endTime) {
    final Duration meetingDur = Duration.between(startTime, endTime);
    return new fildol.MSTeamsAttendance.Duration((int) meetingDur.toSeconds());
  }

  public static LocalDateTime parseDateTime(String startTime) {
    return LocalDateTime.parse(startTime, FORMAT);
  }
}
