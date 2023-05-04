package com.northconcepts.datapipeline.examples.parquet;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

import java.io.File;

public class GenerateParquetSchemaByAnalyzingAllRecords {

    private static final File PARQUET_FILE = new File("example/data/output/GenerateParquetSchemaForAllRecords.parquet");

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/1000_Sales_Records.csv"))
                .setFieldNamesInFirstRow(true);

        ParquetDataWriter writer = new ParquetDataWriter(PARQUET_FILE);
        writer.setMaxRecordsAnalyzed(null);
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
    
}
