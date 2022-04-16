package fildol.MSTeamsAttendance;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Invalid arguments.");
            System.out.println("Syntax: attendanceReport.csv requiredDurationFraction");
            System.out.println("Example: attendanceReport.csv 0.5");
            System.exit(-1);
        }
        double requiredDurationFraction = Double.parseDouble(args[1]);
        final Meeting meeting = new Meeting(Path.of(args[0]), requiredDurationFraction);
        System.out.println(meeting);
    }
}
