package com.northconcepts.datapipeline.foundations.examples.time;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.foundations.time.DateTimePatternMatch;
import com.northconcepts.datapipeline.foundations.time.DateTimePatternDetector;

import java.time.temporal.TemporalQueries;
import java.util.List;

public class DetectDate_TimePatternsInStrings {

    public static void main(String[] args) {
        detectDateTimePattern("2001");
        detectDateTimePattern("2001-01");
        detectDateTimePattern("2001-01-12");
        detectDateTimePattern("22-03-1999 05:06:07");
        detectDateTimePattern("1997-07-16T19:20:30.45+01:00");
        detectDateTimePattern("Tuesday Nov 27 23:34:09 UTC 2018");

    }

    public static void detectDateTimePattern(String value){
        DateTimePatternDetector detector = new DateTimePatternDetector();
        List<DateTimePatternMatch> patterns = detector.matchAll(value);

        System.out.println("=======================================================================");
        System.out.println("String: "+value);


        if (patterns.size() == 0) {
            System.out.println("    ------ NO MATCHING PATTERNS ------");
            return;
        }

        for (DateTimePatternMatch pattern : patterns) {
            System.out.print("    " + pattern.getPattern() + "  -->  " +
                    pattern.getValue().query(TemporalQueries.localDate()) + " " +
                    pattern.getValue().query(TemporalQueries.localTime()) + " " +
                    pattern.getValue().query(TemporalQueries.zone()) + " "
            );
            try {
                System.out.println(pattern.getFieldType());
            } catch (DataException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
