package fildol.MSTeamsAttendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import fildol.MSTeamsAttendance.Duration;
import org.junit.jupiter.api.Test;

class DurationTest {

  @Test
  void zero() {
    // GIVEN
    final Duration duration = new Duration();

    // THEN
    assertEquals(0, duration.getDays());
    assertEquals(0, duration.getHours());
    assertEquals(0, duration.getMinutes());
    assertEquals(0, duration.getSeconds());
    assertEquals(0, duration.getTotalSeconds());
    assertEquals("0s", duration.toString());
  }

  @Test
  void parsesSeconds() {
    // GIVEN
    final Duration duration = new Duration("1s");

    // THEN
    assertEquals(0, duration.getDays());
    assertEquals(0, duration.getHours());
    assertEquals(0, duration.getMinutes());
    assertEquals(1, duration.getSeconds());
    assertEquals(1, duration.getTotalSeconds());
    assertEquals("1s", duration.toString());
  }

  @Test
  void parsesMinutes() {
    // GIVEN
    final Duration duration = new Duration("1m");

    // THEN
    assertEquals(0, duration.getDays());
    assertEquals(0, duration.getHours());
    assertEquals(1, duration.getMinutes());
    assertEquals(0, duration.getSeconds());
    assertEquals(60, duration.getTotalSeconds());
    assertEquals("1m", duration.toString());
  }

  @Test
  void parsesHours() {
    // GIVEN
    final Duration duration = new Duration("1h");

    // THEN
    assertEquals(0, duration.getDays());
    assertEquals(1, duration.getHours());
    assertEquals(0, duration.getMinutes());
    assertEquals(0, duration.getSeconds());
    assertEquals(60 * 60, duration.getTotalSeconds());
    assertEquals("1h", duration.toString());
  }

  @Test
  void parsesDays() {
    // GIVEN
    final Duration duration = new Duration("1d");

    // THEN
    assertEquals(1, duration.getDays());
    assertEquals(0, duration.getHours());
    assertEquals(0, duration.getMinutes());
    assertEquals(0, duration.getSeconds());
    assertEquals(24 * 60 * 60, duration.getTotalSeconds());
    assertEquals("1d", duration.toString());
  }

  @Test
  void parsesAll() {
    // GIVEN
    final String durationString = "1d 2h 3m 4s";
    final Duration duration = new Duration(durationString);

    // THEN
    assertEquals(1, duration.getDays());
    assertEquals(2, duration.getHours());
    assertEquals(3, duration.getMinutes());
    assertEquals(4, duration.getSeconds());
    assertEquals(durationString, duration.toString());
  }

  @Test
  void normalizesSeconds() {
    // GIVEN
    final Duration duration = new Duration("61s");

    // THEN
    assertEquals(0, duration.getDays());
    assertEquals(0, duration.getHours());
    assertEquals(1, duration.getMinutes());
    assertEquals(1, duration.getSeconds());
    assertEquals(61, duration.getTotalSeconds());
    assertEquals("1m 1s", duration.toString());
  }

  @Test
  void throwsForInvalid() {
    // GIVEN
    try {
      final Duration duration = new Duration("invalid");
    } catch (IllegalArgumentException ex) {
      return;
    }
    fail("did not throw");
  }

  @Test
  void addsDuration() {
    // GIVEN
    final Duration day = new Duration("1d");
    final Duration second = new Duration("1s");

    // WHEN
    day.add(second);

    // THEN
    assertEquals("1d 1s", day.toString());
  }
}
