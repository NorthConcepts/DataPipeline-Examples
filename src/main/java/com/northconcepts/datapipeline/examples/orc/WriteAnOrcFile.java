/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.orc;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DebugReader;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.orc.OrcDataReader;
import com.northconcepts.datapipeline.orc.OrcDataWriter;

public class WriteAnOrcFile {

    private static final File ORC_FILE = new File("example/data/output/WriteAnOrcFile.orc");

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Write records to an ORC file");
        System.out.println("============================================================");

        DataReader reader = new MemoryReader(createRecordList());
        reader = new DebugReader(reader);
        OrcDataWriter writer = new OrcDataWriter(ORC_FILE);
        Job.run(reader, writer);

        System.out.println("============================================================");
        System.out.println("Prepared Schema");
        System.out.println("============================================================");

        // System.out.println(writer.getSchema()); //TODO this shows compile time exception!!!

        System.out.println("============================================================");
        System.out.println("Read the ORC file");
        System.out.println("============================================================");

        Job.run(new OrcDataReader(ORC_FILE), new StreamWriter(System.out));

    }

    public static RecordList createRecordList() {
        RecordList recordList = new RecordList();

        Record record1 = new Record();
        record1.setField("BLOB", new byte[] { 2, 4, 6, 8, 10, 12 });
        record1.setField("BOOLEAN", true);
        record1.setField("BYTE", (byte) 97);
        record1.setField("CHAR", 'A');
        record1.setField("DATE", Moment.parseMoment("2014-12-25").getDatePart());
        record1.setField("DATETIME", Moment.parseMoment("2014-12-25 13:41:57").getDate());
        record1.setField("DOUBLE", 2048.1024);
        record1.setField("FLOAT", 4096.32f);
        record1.setField("INT", 8192);
        record1.setField("LONG", 1152921504606846976L);
        record1.setField("SHORT", (short) 32);
        record1.setField("BIG_DECIMAL", new BigDecimal("123.456789"));
        record1.setField("BIG_INTEGER", BigInteger.valueOf(123456L));
        record1.setField("STRING", "A basic numeric constant is considered an integer.");
        record1.setField("TIME", Moment.parseMoment("13:41:57").getTimePart());
        record1.setField("Array-2", new String[] { "J", "A", "V", "A" });
        record1.setField("Array-3", new Double[] { 123.123, 345.345, 456.456, 555.678 });
        record1
            .setField("RECORD",
                    new Record()
                        .setField("RECORD",
                                new Record()
                                    .setField("STRING", "A basic numeric constant is considered an integer.")
                                    .setField("DOUBLE", 1234.12345D)));

        // Record with null values.
        Record record2 = new Record();
        record2.setFieldNull("BLOB", FieldType.BLOB);
        record2.setFieldNull("BOOLEAN", FieldType.BOOLEAN);
        record2.setFieldNull("BYTE", FieldType.BYTE);
        record2.setFieldNull("CHAR", FieldType.CHAR);
        record2.setFieldNull("DATE", FieldType.DATE);
        record2.setFieldNull("DATETIME", FieldType.DATETIME);
        record2.setFieldNull("DOUBLE", FieldType.DOUBLE);
        record2.setFieldNull("FLOAT", FieldType.FLOAT);
        record2.setFieldNull("INT", FieldType.INT);
        record2.setFieldNull("LONG", FieldType.LONG);
        record2.setFieldNull("SHORT", FieldType.SHORT);
        record2.setFieldNull("BIG_DECIMAL", FieldType.BIG_DECIMAL);
        record2.setFieldNull("BIG_INTEGER", FieldType.BIG_INTEGER);
        record2.setFieldNull("STRING", FieldType.STRING);
        record2.setFieldNull("TIME", FieldType.TIME);
        record2.setFieldNull("Array-2", FieldType.STRING);
        record2.setFieldNull("Array-3", FieldType.DOUBLE);
        record2.setFieldNull("RECORD", FieldType.RECORD);

        recordList.add(record1, record2);
        return recordList;
    }
}
