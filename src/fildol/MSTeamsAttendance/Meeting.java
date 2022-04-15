package fildol.MSTeamsAttendance;

import static java.lang.String.format;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Meeting summary */
public class Meeting {

  public static final String TAB = "\t";
  static Pattern pattern = Pattern.compile("(\\d+)d (\\d+)h (\\d+)m (\\d+)s");

  private final ReportParser reportParser;
  private final double requiredDurationFraction;

  public Meeting(Path csvFile, double requiredDurationFraction) {
    if (requiredDurationFraction < 0 || requiredDurationFraction > 1) {
      throw new IllegalArgumentException(format("Invalid fraction: %f", requiredDurationFraction));
    }
    this.requiredDurationFraction = requiredDurationFraction;

    this.reportParser = new ReportParser(csvFile, requiredDurationFraction);

    reportParser.buildParticipant();
  }

  @Override
  public String toString() {
    return "Total participants - over "
        + requiredDurationFraction * 100
        + "% of full meeting time: "
        + getQualifyingParticipantCount();
  }

  public int getQualifyingParticipantCount() {
    return reportParser.participants.size();
  }

  static void add(String d, int[] p) {
    Matcher m = pattern.matcher(d);
    m.find();
    for (int i = 0; i < p.length; i++) {
      p[i] += Integer.parseInt(m.group(i + 1));
    }
  }

  static String addDuration(String dur1, String dur2) {
    int[] placeholder = new int[4];
    String[] durations1 = completeDuration(dur1);
    String d1 = String.join(" ", durations1[0], durations1[1], durations1[2], durations1[3]);
    String[] durations2 = completeDuration(dur2);
    String d2 = String.join(" ", durations2[0], durations2[1], durations2[2], durations2[3]);
    add(d1, placeholder);
    add(d2, placeholder);
    if (placeholder[3] >= 60) {
      placeholder[3] -= 60;
      placeholder[2]++;
    }
    if (placeholder[2] >= 60) {
      placeholder[2] -= 60;
      placeholder[1]++;
    }
    if (placeholder[1] >= 24) {
      placeholder[1] -= 24;
      placeholder[0]++;
    }
    return format(
        "%dd %dh %dm %ds", placeholder[0], placeholder[1], placeholder[2], placeholder[3]);
  }

  private static String[] completeDuration(String d1) {
    String[] ds1 = d1.split(" ");
    boolean days = false;
    boolean hours = false;
    boolean minutes = false;
    boolean seconds = false;
    String[] dN = new String[4];
    for (String ds : ds1) {
      ds = ds.replace(" ", "");
      if (ds.contains("d")) {
        days = true;
        dN[0] = ds;
      } else if (ds.contains("h")) {
        hours = true;
        dN[1] = ds;
      } else if (ds.contains("m")) {
        minutes = true;
        dN[2] = ds;
      } else if (ds.contains("s")) {
        seconds = true;
        dN[3] = ds;
      }
    }
    if (!days) {
      dN[0] = "0d";
    }
    if (!hours) {
      dN[1] = "0h";
    }
    if (!minutes) {
      dN[2] = "0m";
    }
    if (!seconds) {
      dN[3] = "0s";
    }
    return dN;
  }
}
