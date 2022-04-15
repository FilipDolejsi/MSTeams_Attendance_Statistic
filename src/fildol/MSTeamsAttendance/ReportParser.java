package fildol.MSTeamsAttendance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ReportParser {

  final MSTeamsMeetingAttendanceParser meetingDurationParser =
      new MSTeamsMeetingAttendanceParser();
  final List<Participant> participants = new ArrayList<>();
  final Path csvFile;
  /**
   * percentage of the meeting duration that a participant needs to be counted in the sum (e.g. 10%
   * = 0.1)
   */
  final double requiredDurationFraction;

  public ReportParser(Path csvFile, double requiredDurationFraction) {
    this.csvFile = csvFile;
    this.requiredDurationFraction = requiredDurationFraction;
  }

  public void buildParticipant() {
    String stringLine;
    String endTime;
    String meetingDuration = null;
    String startTime = null;
    int rowCounter = 1;
    try (BufferedReader bufferedReader =
        new BufferedReader(new FileReader(csvFile.toFile(), StandardCharsets.UTF_16LE))) {
      while ((stringLine = bufferedReader.readLine()) != null) {
        boolean found = false;
        String[] employee = stringLine.split(Meeting.TAB);
        StringTokenizer stringTokenizer = new StringTokenizer(stringLine, Meeting.TAB);
        if (stringTokenizer.hasMoreTokens()) {
          stringTokenizer.nextToken();
        }
        if (rowCounter == 4) {
          stringTokenizer.nextToken(", ");
          startTime = stringTokenizer.nextToken(", ") + " " + stringTokenizer.nextToken();
        }
        if (rowCounter == 5) {
          stringTokenizer.nextToken(", ");
          endTime = stringTokenizer.nextToken(", ") + " " + stringTokenizer.nextToken();
          meetingDuration = meetingDurationParser.formatMeetingDuration(startTime, endTime);
        }
        if (rowCounter >= 9 && stringTokenizer.countTokens() > 5) {
          for (Participant p : participants) {
            if (employee[6].equals(p.getId())) {
              p.setDuration(Meeting.addDuration(p.getDuration(), employee[3]));
              found = true;
            }
          }
          if (!found) {
            StringTokenizer sT = new StringTokenizer(stringLine, Meeting.TAB);
            participants.add(
                new Participant(
                    sT.nextToken(),
                    sT.nextToken(),
                    sT.nextToken(),
                    sT.nextToken(),
                    sT.nextToken(),
                    sT.nextToken(),
                    sT.nextToken()));
          }
        }
        rowCounter++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    Iterator<Participant> itr = participants.iterator();
    while (itr.hasNext()) {
      Participant p = itr.next();
      String duration = Meeting.addDuration(p.getDuration(), "0d 0h 0m 0s");
      int[] placeholder = new int[4];
      Meeting.add(duration, placeholder);
      String[] meetDuration = meetingDuration.split(" ");
      if (meetingDurationParser.timeToInt(placeholder)
          < requiredDurationFraction * Integer.parseInt(meetDuration[0])) {
        itr.remove();
      }
    }
  }
}