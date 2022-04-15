package fildol.MSTeamsAttendance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Meeting {
    static Pattern pattern = Pattern.compile("(\\d+)d (\\d+)h (\\d+)m (\\d+)s");
    private final MSTeamsMeetingAttendanceParser meetingDurationParser = new MSTeamsMeetingAttendanceParser();
    ArrayList<Participant> participants;
    String csvFile;
    double percentageNeeded; //percentage of the meeting duration that a participant needs to be counted in the sum (eg. 10% = 0.1)

    public Meeting(String csvFile, String percentageNeeded) {
        // write your code here
        this.participants = new ArrayList<>();
        this.csvFile = csvFile;
        this.percentageNeeded = Double.parseDouble(percentageNeeded);
    }

    @Override
    public String toString() {
        return "Total participants - over " + percentageNeeded * 100 + "% of full meeting time: " + participants.size();
    }

    public void buildParticipant() {
        String stringLine;
        String endTime;
        String meetingDuration = null;
        String startTime = null;
        int rowCounter = 1;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile, StandardCharsets.UTF_16LE))) {
            while ((stringLine = bufferedReader.readLine()) != null) {
                boolean found = false;
                String[] employee = stringLine.split("\t");
                StringTokenizer stringTokenizer = new StringTokenizer(stringLine, "\t");
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
                    for (Participant p :
                            participants) {
                        if (employee[6].equals(p.getId())) {
                            p.setDuration(addDuration(p.getDuration(), employee[3]));
                            found = true;
                        }
                    }
                    if (!found) {
                        StringTokenizer sT = new StringTokenizer(stringLine, "\t");
                        participants.add(new Participant(sT.nextToken(), sT.nextToken(), sT.nextToken(), sT.nextToken(), sT.nextToken(), sT.nextToken(), sT.nextToken()));
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
            String duration = addDuration(p.getDuration(), "0d 0h 0m 0s");
            int[] placeholder = new int[4];
            add(duration, placeholder);
            String[] meetDuration = meetingDuration.split(" ");
            if (meetingDurationParser.timeToInt(placeholder) < percentageNeeded * Integer.parseInt(meetDuration[0])) {
                itr.remove();
            }
        }
    }

    private static void add(String d, int[] p) {
        Matcher m = pattern.matcher(d);
        m.find();
        for (int i = 0; i < p.length; i++) {
            p[i] += Integer.parseInt(m.group(i + 1));
        }
    }

    private static String addDuration(String dur1, String dur2) {
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
        return String.format("%dd %dh %dm %ds", placeholder[0], placeholder[1], placeholder[2], placeholder[3]);
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
