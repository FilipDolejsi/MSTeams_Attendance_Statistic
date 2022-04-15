package fildol.MSTeamsAttendance;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class DateTimeParserTest {

  @Test
  void parsesFullDateTime() {
    // GIVEN
    final String input = "2/18/2022, 2:47:02 PM";

    // WHEN
    final LocalDateTime localTime = DateTimeParser.parseDateTime(input);

    // THEN
    assertEquals(14, localTime.getHour());
    assertEquals(47, localTime.getMinute());
  }
}