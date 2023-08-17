package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonReader;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

import java.io.File;

public class ConvertJsonToParquet {
    private static final File PARQUET_FILE = new File("example/data/output/WriteAParquetFileFromJson.parquet");

    public static void main(String[] args) {
        File inputFile = new File("example/data/input/pipeline/MOCK_DATA.json");
        DataReader reader = new SimpleJsonReader(inputFile);

        ParquetDataWriter writer = new ParquetDataWriter(PARQUET_FILE);

        Job.run(reader, writer);

        System.out.println("=======================Reading Parquet File============================================");
        reader = new ParquetDataReader(PARQUET_FILE);
        Job.run(reader, new StreamWriter(System.out));
    }
}
