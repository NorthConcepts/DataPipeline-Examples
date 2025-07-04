package com.northconcepts.datapipeline.foundations.examples.difference;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.difference.RecordDiff;

public class CompareRecords {

    public static void main(String[] args) {
        Record oldRecord = new Record()
                .setField("name", "John Doe")
                .setField("dob", LocalDate.parse("2000-01-01"))
                .setField("languages", new String[] {"English", "French"})
                .setField("height", 1.70)
                .setField("netIncome", new BigDecimal(100_286.99));

        Record newRecord = new Record()
                .setField("name", "John Doe")
                .setField("age", 24)
                .setField("languages", new String[] {"English", "French", "Spanish"})
                .setField("height", 1.73)
                .setField("netIncome", new BigDecimal(120_286.99));

        RecordDiff diff = RecordDiff.diff("userRecord", oldRecord, newRecord, "height", "netIncome");

        // Diff will report following changes:
        // name - NONE
        // dob - REMOVED
        // age - ADDED
        // English - NONE
        // French - NONE
        // Spanish - ADDED
        // height and newIncome will not be reported as they are excluded from comparison.

        System.out.println("Record Diff: " + diff);
        System.out.println("----------------------");
        System.out.println("Record Diff: " + diff.toRecord());
        System.out.println("----------------------");
        System.out.println("Record Diff: " + diff.toJson());
    }
}
