package com.northconcepts.datapipeline.examples.orc;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Date;

import org.apache.orc.CompressionKind;

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

public class CompressAnOrcFile {

    private static final File ORC_FILE = new File("example/data/output/CompressAnOrcFile.orc");

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Write records to a parquet file");
        System.out.println("============================================================");
        
        DataReader reader = new MemoryReader(createRecordList());
        reader = new DebugReader(reader);
        OrcDataWriter writer = new OrcDataWriter(ORC_FILE);
        
        //Supported compression are: NONE, ZLIB, SNAPPY, LZO, LZ4, ZSTD
        writer.setCompressionKind(CompressionKind.ZLIB);

        Job.run(reader, writer);

        System.out.println("============================================================");
        System.out.println("Prepared Schema");
        System.out.println("============================================================");
        
        System.out.println(writer.getSchema());

        System.out.println("============================================================");
        System.out.println("Read the parquet file");
        System.out.println("============================================================");
        
        Job.run(new OrcDataReader(ORC_FILE), new StreamWriter(System.out));

    }

    public static RecordList createRecordList() {
        RecordList recordList = new RecordList();

        Record record1 = new Record();
        record1.setField("BLOB", new byte[][] {{ 2, 4, 6, 8, 10, 12 }, { 5, 23, 34, 65, 1 }});
        record1.setField("BOOLEAN", new boolean[] { true, false, false, true });
        record1.setField("BYTE", new Byte[] { 12, 96 });
        record1.setField("CHAR", new char[] { 'Z', 'B' });
        record1.setField("DATE", new java.sql.Date[] { Moment.parseMoment("2014-12-25").getDatePart(), Moment.parseMoment("2021-02-20").getDatePart() });
        record1.setField("DATETIME", new Date[] { Moment.parseMoment("2014-12-25 13:41:57").getDate(), Moment.parseMoment("2021-02-20 21:41:57").getDate() });
        record1.setField("DOUBLE", new Double[] { 20481.102412, 123.680 });
        record1.setField("FLOAT", new Float[] { 14096.3212f });
        record1.setField("INT", new int[] { 8192, 123 });
        record1.setField("LONG", new long[] { 1152921504606846976L });
        record1.setField("SHORT", new short[] { 32, 43 });
        record1.setField("BIG_DECIMAL", new BigDecimal[] { new BigDecimal("456789.123") });
        record1.setField("BIG_INTEGER", new BigInteger[] { BigInteger.valueOf(123456890L), BigInteger.valueOf(765) });
        record1.setField("STRING", new String[] { "A basic numeric constant is considered an integer.", "It's a miracle." });
        record1.setField("TIME", new Time[] { Moment.parseMoment("00:41:57").getTimePart(), Moment.parseMoment("21:34:22").getTimePart() });
        record1.setField("Array-2", new String[] { "A", "B", "C", "D" });
        record1.setField("Array-3", new Double[] { 888.6, 453.12, 897.456, 342.678 });
        record1
            .setField("RECORD", new Record[] {
                    new Record()
                        .setField("RECORD",
                                new Record()
                                    .setField("STRING", "A 1st basic numeric constant is considered an integer.")
                                    .setField("DOUBLE", 123.456)),
                    new Record()
                        .setField("RECORD",
                                new Record()
                                    .setField("STRING", "A 2nd basic numeric constant is considered an integer.")
                                    .setField("DOUBLE", 23.4567)),
                    new Record()
                        .setField("RECORD",
                                new Record()
                                    .setField("STRING", "A 3rd basic numeric constant is considered an integer.")
                                    .setField("DOUBLE", 3.45678))
            });

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
