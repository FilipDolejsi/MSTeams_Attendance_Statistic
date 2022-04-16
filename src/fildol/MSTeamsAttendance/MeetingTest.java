package fildol.MSTeamsAttendance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

class MeetingTest {

  @org.junit.jupiter.api.Test
  void oneParticipantAttendedFullMeeting() {

    // WHEN
    final Meeting meeting = new Meeting(Path.of("demo.csv"), 1d);

    // THEN
    assertEquals(1, meeting.getQualifyingParticipantCount());
  }

  @org.junit.jupiter.api.Test
  void oneParticipantAttendsAtLeastHalf() {

    // WHEN
    final Meeting meeting = new Meeting(Path.of("demo.csv"), .5);

    // THEN
    assertEquals(2, meeting.getQualifyingParticipantCount());
  }

  @org.junit.jupiter.api.Test
  void allParticipantsAttendAtLeastZero() {

    // WHEN
    final Meeting meeting = new Meeting(Path.of("demo.csv"), 0d);

    // THEN
    assertEquals(3, meeting.getQualifyingParticipantCount());
  }
}
