package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;
import java.math.BigInteger;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DebugReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class ReadAndWriteUnsignedNumbersInParquet {

    private static final File PARQUET_FILE = new File("example/data/output/ReadAndWriteUnsignedNumbersInParquet.parquet");

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Write records to a parquet file");
        System.out.println("============================================================");

        String schema = "message schema {" +
                "  optional int32 unsigned_field_8 (INTEGER(8,false)); " +
                "  optional int32 signed_field_8 (INTEGER(8,true)); " +
                "  optional int32 unsigned_field_16 (INTEGER(16,false)); " +
                "  optional int32 signed_field_16 (INTEGER(16,true)); " +
                "  optional int32 unsigned_field_32 (INTEGER(32,false)); " +
                "  optional int32 signed_field_32 (INTEGER(32,true)); " +
                "  optional int64 unsigned_field_64 (INTEGER(64,false)); " +
                "  optional int64 signed_field_64 (INTEGER(64,true)); " +
                "}";

        DataReader reader = new MemoryReader(createRecordList());
        reader = new DebugReader(reader);
        ParquetDataWriter writer = new ParquetDataWriter(PARQUET_FILE);
        writer.setSchema(schema);
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
        record1.setField("unsigned_field_8", 255);
        record1.setField("signed_field_8", 127);
        record1.setField("unsigned_field_16", 65535);
        record1.setField("signed_field_16", 32767);
        record1.setField("unsigned_field_32", 4294967295L);
        record1.setField("signed_field_32", 2147483647);
        record1.setField("unsigned_field_64", new BigInteger("18446744073709551615"));
        record1.setField("signed_field_64", 9223372036854775807L);


        Record record2 = new Record();
        record2.setField("unsigned_field_8", 127);
        record2.setField("signed_field_8", -127);
        record2.setField("unsigned_field_16", 65535);
        record2.setField("signed_field_16", -32767);
        record2.setField("unsigned_field_32", 4294967295L);
        record2.setField("signed_field_32", -2147483647);
        record2.setField("unsigned_field_64", new BigInteger("18446744073709551615"));
        record2.setField("signed_field_64", -9223372036854775807L);

        recordList.add(record1, record2);
        return recordList;
    }
}
