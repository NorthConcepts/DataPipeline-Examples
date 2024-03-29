package com.northconcepts.datapipeline.foundations.examples.difference;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.difference.RecordDiff;

public class SaveDiffToAndFromRecord {

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

        RecordDiff oldDiff = RecordDiff.diff("userRecord", oldRecord, newRecord, "height", "netIncome");
        System.out.println("old diff: " + oldDiff);

        System.out.println("===============================================================");

        Record diffRecord = oldDiff.toRecord();
        System.out.println(diffRecord);

        System.out.println("===============================================================");

        RecordDiff newDiff = new RecordDiff().fromRecord(diffRecord);
        System.out.println("new diff: " + newDiff);
    }
}
