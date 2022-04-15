package fildol.MSTeamsAttendance;

import static fildol.MSTeamsAttendance.DateTimeParser.parseDateTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ReportParser {

  private static final String MEETING_SUMMARY = "Meeting Summary";
  private static final String TAB = "\t";
  private final Map<String, Participant> participants = new HashMap<>();
  final Path csvFile;

  private Duration meetingDuration;
  private int totalNumberOfParticipants;
  private String meetingTitle;
  private LocalDateTime meetingStartTime;
  private LocalDateTime meetingEndTime;
  private String meetingId;

  public ReportParser(Path csvFile) {
    this.csvFile = csvFile;
  }

  public void buildParticipant() throws IOException {
    try (BufferedReader bufferedReader =
        new BufferedReader(new FileReader(csvFile.toFile(), StandardCharsets.UTF_16LE))) {

      // parse the report headers
      final HashMap<String, String> headers = parseHeader(bufferedReader);
      this.totalNumberOfParticipants =
          Integer.parseInt(headers.get("Total Number of Participants"));
      this.meetingTitle = headers.get("Meeting Title");
      this.meetingStartTime = parseDateTime(headers.get("Meeting Start Time"));
      this.meetingEndTime = parseDateTime(headers.get("Meeting End Time"));
      this.meetingDuration =
          DateTimeParser.computeMeetingDuration(meetingStartTime, meetingEndTime);
      this.meetingId = headers.get("Meeting Id");

      // parse the table of participants
      parseTable(bufferedReader);
    }
  }

  private CharBuffer decodeUTF16LE(String line) {
    return StandardCharsets.UTF_16.decode(StandardCharsets.UTF_16LE.encode(line));
  }

  private CharBuffer decodeUTF16(String line) {
    return StandardCharsets.UTF_16.decode(StandardCharsets.UTF_16.encode(line));
  }

  private HashMap<String, String> parseHeader(BufferedReader bufferedReader) throws IOException {
    String line;

    if ((line = bufferedReader.readLine()) != null) {
      if (!decodeUTF16LE(line).equals(decodeUTF16(MEETING_SUMMARY))) {
        throw new IllegalArgumentException(
            "First row should contain " + MEETING_SUMMARY + ", but was " + line);
      }
    }

    final HashMap<String, String> keyValueMap = new HashMap<>();

    while ((line = bufferedReader.readLine()) != null) {

      if (line.trim().isEmpty()) {
        // done reading the header
        return keyValueMap;
      }

      StringTokenizer stringTokenizer = new StringTokenizer(line, TAB);
      if (stringTokenizer.countTokens() == 2) {
        final String key = stringTokenizer.nextToken();
        final String value = stringTokenizer.nextToken();

        keyValueMap.put(key, value);
      } else {
        throw new IllegalArgumentException("Invalid header row: " + line);
      }
    }

    return keyValueMap;
  }

  private void parseTable(BufferedReader bufferedReader) throws IOException {
    String line;

    final HashMap<String, Integer> tableHeaders = new HashMap<>();

    if ((line = bufferedReader.readLine()) != null) {

      final String[] headers = line.split(TAB);

      for (int i = 0; i < headers.length; i++) {
        tableHeaders.put(headers[i], i);
      }
    }

    while ((line = bufferedReader.readLine()) != null) {

      final Participant participant = parseParticipant(line, tableHeaders);
      final Participant matchingParticipant = participants.get(participant.getId());

      if (matchingParticipant != null) {
        matchingParticipant.getDuration().add(participant.getDuration());
      } else {
        participants.put(participant.getId(), participant);
      }
    }
  }

  private Participant parseParticipant(
      String participantLine, HashMap<String, Integer> tableHeaders) {
    final String[] values = participantLine.split(TAB);
    return new Participant(
        values[tableHeaders.get("Full Name")],
        parseDateTime(values[tableHeaders.get("Join Time")]),
        parseDateTime(values[tableHeaders.get("Leave Time")]),
        new Duration(values[tableHeaders.get("Duration")]),
        values[tableHeaders.get("Email")],
        values[tableHeaders.get("Role")],
        values[tableHeaders.get("Participant ID (UPN)")]);
  }

  public Map<String, Participant> getParticipants() {
    return participants;
  }

  public Duration getMeetingDuration() {
    return this.meetingDuration;
  }

  public int getTotalNumberOfParticipants() {
    return totalNumberOfParticipants;
  }

  public String getMeetingTitle() {
    return meetingTitle;
  }
}
