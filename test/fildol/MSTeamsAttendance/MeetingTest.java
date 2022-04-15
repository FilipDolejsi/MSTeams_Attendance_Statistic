package fildol.MSTeamsAttendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import fildol.MSTeamsAttendance.Meeting;
import fildol.MSTeamsAttendance.Participant;
import java.io.IOException;
import java.nio.file.Path;

class MeetingTest {

  @org.junit.jupiter.api.Test
  void parsesAllParticipants() throws IOException {

    // WHEN
    final Meeting meeting = new Meeting(Path.of("demo.csv"), 0d);

    // THEN
    assertEquals(3, meeting.getParticipants().size());
    assertParticipantEquals(meeting.getParticipants().get("PBobby@jingle.com"), "1h 15m");
    assertParticipantEquals(meeting.getParticipants().get("MJacob@jingle.com"), "14s");
    assertParticipantEquals(meeting.getParticipants().get("JBarac@jingle.com"), "49m 48s");
  }

  private void assertParticipantEquals(Participant participant, String expectedDuration) {
    assertNotNull(participant);
    assertEquals(participant.getDuration().toString(), expectedDuration);
  }

  @org.junit.jupiter.api.Test
  void noParticipantAttendedFullMeeting() throws IOException {

    // WHEN
    final Meeting meeting = new Meeting(Path.of("demo.csv"), 1d);

    // THEN
    assertEquals(0, meeting.getQualifyingParticipantCount());
  }

  @org.junit.jupiter.api.Test
  void oneParticipantAttendsAtLeastHalf() throws IOException {

    // WHEN
    final Meeting meeting = new Meeting(Path.of("demo.csv"), .5);

    // THEN
    assertEquals(2, meeting.getQualifyingParticipantCount());
  }

  @org.junit.jupiter.api.Test
  void allParticipantsAttendAtLeastZero() throws IOException {

    // WHEN
    final Meeting meeting = new Meeting(Path.of("demo.csv"), 0d);

    // THEN
    assertEquals(3, meeting.getQualifyingParticipantCount());
  }
}
