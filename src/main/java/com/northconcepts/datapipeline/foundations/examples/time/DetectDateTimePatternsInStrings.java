package com.northconcepts.datapipeline.foundations.examples.time;

import com.northconcepts.datapipeline.foundations.time.DateTimePatternDetector;
import com.northconcepts.datapipeline.foundations.time.DateTimePatternMatch;

import java.time.temporal.TemporalQueries;
import java.util.List;

public class DetectDateTimePatternsInStrings {

    public static void main(String[] args) {
        detectDateTimePattern("20");
        detectDateTimePattern("2001");
        detectDateTimePattern("2001-01");
        detectDateTimePattern("2001-01-12");
        detectDateTimePattern("03/04/05");
        detectDateTimePattern("22-03-1999 05:06:07");
        detectDateTimePattern("1997-07-16T19:20:30.45+01:00");
        detectDateTimePattern("Tuesday Nov 27 23:34:09 UTC 2018");
    }

    public static void detectDateTimePattern(String value){
        DateTimePatternDetector detector = new DateTimePatternDetector();
        List<DateTimePatternMatch> patterns = detector.matchAll(value);

        System.out.println("=======================================================================");
        System.out.println("Input: " + value);
        System.out.println("=======================================================================");

        if (patterns.size() == 0) {
            System.out.println("    ------ NO MATCHING PATTERNS ------");
            System.out.println();
            return;
        }

        for (DateTimePatternMatch pattern : patterns) {
            try {
                System.out.println("    Pattern:   " + pattern.getPattern());
                System.out.println("    LocalDate: " + pattern.getValue().query(TemporalQueries.localDate()));
                System.out.println("    LocalTime: " + pattern.getValue().query(TemporalQueries.localTime()));
                System.out.println("    Zone:      " + pattern.getValue().query(TemporalQueries.zone()));
                
                System.out.print("    FieldType: ");
                System.out.println(pattern.getFieldType());
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
            System.out.println();
        }
    }
}
