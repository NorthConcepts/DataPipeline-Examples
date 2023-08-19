package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonReader;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

import java.io.File;

public class ConvertUnstructuredJsonToParquet {
    private static final File PARQUET_FILE = new File("example/data/output/WriteAParquetFileFromUnstructuredJson.parquet");

    public static void main(String[] args) {
        DataReader reader = new JsonReader(new File("example/data/input/finance.json"))
                .addField("symbol", "//array/object/t")
                .addField("exchange", "//array/object/e")
                .addField("price", "//array/object/l")
                .addField("change", "//array/object/c")
                .addRecordBreak("//array/object");

        ParquetDataWriter writer = new ParquetDataWriter(PARQUET_FILE);

        Job.run(reader, writer);

        System.out.println("=======================Reading Parquet File============================================");
        reader = new ParquetDataReader(PARQUET_FILE);
        Job.run(reader, new StreamWriter(System.out));
    }
}
