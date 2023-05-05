package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class GenerateParquetSchemaByAnalyzingInitialRecords {

    private static final long INITIAL_RECORDS_TO_ANALYZE = 500L;
    
    private static final File PARQUET_FILE = new File("example/data/output/GenerateParquetSchemaFor500Records.parquet");

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/1000_Sales_Records.csv"))
                .setFieldNamesInFirstRow(true);
        
        reader = fixFieldNames(reader);

        ParquetDataWriter writer = new ParquetDataWriter(PARQUET_FILE);
        writer.setMaxRecordsAnalyzed(INITIAL_RECORDS_TO_ANALYZE);
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
    
    private static ProxyReader fixFieldNames(DataReader reader) {
        return new ProxyReader(reader){
            @Override
            protected Record interceptRecord(Record record) throws Throwable {
                for (Field field : record) {
                    field.setName(field.getName().replace(" ", "_"));
                }
                return record;
            }
        };
    }
    
}
