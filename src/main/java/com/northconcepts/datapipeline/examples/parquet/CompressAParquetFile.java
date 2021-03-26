package com.northconcepts.datapipeline.examples.parquet;

import com.northconcepts.datapipeline.core.*;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;

public class CompressAParquetFile {

    private static final File PARQUET_FILE = new File("WriteAParquetFile.parquet");

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Write records to a parquet file");
        System.out.println("============================================================");
        
        DataReader reader = new MemoryReader(createRecordList());
        reader = new DebugReader(reader);
        ParquetDataWriter writer = new ParquetDataWriter(PARQUET_FILE);
        writer.setCompressionCodecName(CompressionCodecName.GZIP);

        Job.run(reader, writer);

        System.out.println("============================================================");
        System.out.println("Prepared Schema");
        System.out.println("============================================================");
        
        System.out.println(writer.getSchema());

        System.out.println("============================================================");
        System.out.println("Read the parquet file");
        System.out.println("============================================================");
        
        Job.run(new ParquetDataReader(PARQUET_FILE), new StreamWriter(System.out));

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
        record1.setField("Array-1", Arrays.asList("J", 234, new BigDecimal("456.789"), "A"));
        record1.setField("Array-2", new String[] { "J", "A", "V", "A" });
        record1.setField("Array-3", new Double[] { 123.123, 345.345, 456.456, 555.678 });
        record1
            .setField("RECORD",
                    new Record()
                        .setField("RECORD",
                                new Record()
                                    .setField("STRING", "A basic numeric constant is considered an integer.")
                                    .setField("DOUBLE", 1234.12345D)));

        Record record2 = new Record();
        record2.setField("BLOB", new byte[] { 2, 4, 6, 8, 10, 12 });
        record2.setField("BOOLEAN", new boolean[] { true, false, false, true });
        record2.setField("BYTE", new Byte[] { 12, 96 });
        record2.setField("CHAR", new char[] { 'Z', 'B' });
        record2.setField("DATE", new java.sql.Date[] { Moment.parseMoment("2014-12-25").getDatePart(), Moment.parseMoment("2021-02-20").getDatePart() });
        record2.setField("DATETIME", new Date[] { Moment.parseMoment("2014-12-25 13:41:57").getDate(), Moment.parseMoment("2021-02-20 21:41:57").getDate() });
        record2.setField("DOUBLE", new Double[] { 20481.102412, 123.680 });
        record2.setField("FLOAT", new Float[] { 14096.3212f });
        record2.setField("INT", new int[] { 8192, 123 });
        record2.setField("LONG", new long[] { 1152921504606846976L });
        record2.setField("SHORT", new short[] { 32, 43 });
        record2.setField("BIG_DECIMAL", new BigDecimal[] { new BigDecimal("456789.123") });
        record2.setField("BIG_INTEGER", new BigInteger[] { BigInteger.valueOf(123456890L), BigInteger.valueOf(765) });
        record2.setField("STRING", new String[] { "A basic numeric constant is considered an integer.", "It's a miracle." });
        record2.setField("TIME", new Time[] { Moment.parseMoment("00:41:57").getTimePart(), Moment.parseMoment("21:34:22").getTimePart() });
        record2.setField("Array-1", Arrays.asList("J", 123, new BigDecimal("123.123"), "A"));
        record2.setField("Array-2", new String[] { "A", "B", "C", "D" });
        record2.setField("Array-3", new Double[] { 888.6, 453.12, 897.456, 342.678 });
        record2
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
        Record record3 = new Record();
        record3.setFieldNull("BLOB", FieldType.BLOB);
        record3.setFieldNull("BOOLEAN", FieldType.BOOLEAN);
        record3.setFieldNull("BYTE", FieldType.BYTE);
        record3.setFieldNull("CHAR", FieldType.CHAR);
        record3.setFieldNull("DATE", FieldType.DATE);
        record3.setFieldNull("DATETIME", FieldType.DATETIME);
        record3.setFieldNull("DOUBLE", FieldType.DOUBLE);
        record3.setFieldNull("FLOAT", FieldType.FLOAT);
        record3.setFieldNull("INT", FieldType.INT);
        record3.setFieldNull("LONG", FieldType.LONG);
        record3.setFieldNull("SHORT", FieldType.SHORT);
        record3.setFieldNull("BIG_DECIMAL", FieldType.BIG_DECIMAL);
        record3.setFieldNull("BIG_INTEGER", FieldType.BIG_INTEGER);
        record3.setFieldNull("STRING", FieldType.STRING);
        record3.setFieldNull("TIME", FieldType.TIME);
        record3.setFieldNull("Array-1", FieldType.UNDEFINED);
        record3.setFieldNull("Array-2", FieldType.STRING);
        record3.setFieldNull("Array-3", FieldType.DOUBLE);
        record3.setFieldNull("RECORD", FieldType.RECORD);

        recordList.add(record1, record2, record3);
        return recordList;
    }
}
