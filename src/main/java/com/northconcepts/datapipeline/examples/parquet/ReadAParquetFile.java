package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;

public class ReadAParquetFile {

    public static void main(String[] args) {
        ParquetDataReader reader = new ParquetDataReader(new File("example/data/input/read_parquet_file.parquet"));
        Job.run(reader, new StreamWriter(System.out));

        System.out.println(reader.getSchema());
    }
}
