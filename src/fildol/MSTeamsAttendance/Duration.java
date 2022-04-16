package fildol.MSTeamsAttendance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Duration {

  private int days;
  private int hours;
  private int minutes;
  private int seconds;
  private int totalSeconds;

  static Pattern pattern =
      Pattern.compile("^((\\d+)d)?\\s*((\\d+)h)?\\s*((\\d+)m)?\\s*((\\d+)s)?$");

  public Duration() {}

  public Duration(String durationString) {
    final Matcher m = pattern.matcher(durationString);
    final boolean success = m.find();
    if (!success) {
      throw new IllegalArgumentException("Invalid duration: " + durationString);
    }

    days = m.group(1) != null ? Integer.parseInt(m.group(2)) : 0;
    hours = m.group(3) != null ? Integer.parseInt(m.group(4)) : 0;
    minutes = m.group(5) != null ? Integer.parseInt(m.group(6)) : 0;
    seconds = m.group(7) != null ? Integer.parseInt(m.group(8)) : 0;

    calculateTotalSeconds();
    setTotalSeconds(this.totalSeconds);
  }

  public Duration(int totalSeconds) {
    setTotalSeconds(totalSeconds);
  }

  private void calculateTotalSeconds() {
    this.totalSeconds = seconds + 60 * minutes + 60 * 60 * hours + 24 * 60 * 60 * days;
  }

  private void setTotalSeconds(int totalSeconds) {
    this.totalSeconds = totalSeconds;
    this.seconds = totalSeconds % 60;
    int remainingMinutes = totalSeconds / 60;
    this.minutes = remainingMinutes % 60;
    int remainingHours = remainingMinutes / 60;
    this.hours = remainingHours % 24;
    this.days = remainingHours / 24;
  }

  public int getDays() {
    return this.days;
  }

  public int getHours() {
    return this.hours;
  }

  public int getMinutes() {
    return this.minutes;
  }

  public int getSeconds() {
    return this.seconds;
  }

  public int getTotalSeconds() {
    return this.totalSeconds;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();

    if (totalSeconds == 0) {
      sb.append("0s");
    } else {
      appendAmount(sb, days, 'd');
      appendAmount(sb, hours, 'h');
      appendAmount(sb, minutes, 'm');
      appendAmount(sb, seconds, 's');
    }

    return sb.toString();
  }

  private void appendAmount(StringBuilder sb, int amount, char unit) {
    if (amount > 0) {
      if (sb.length() > 0) {
        sb.append(' ');
      }
      sb.append(amount).append(unit);
    }
  }

  public void add(Duration other) {
    this.setTotalSeconds(totalSeconds + other.totalSeconds);
  }
}
