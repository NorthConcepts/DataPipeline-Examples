package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DebugReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class WriteAParquetFileUsingSchemaFromData {

    private static final File PARQUET_FILE = new File("example/data/output/WriteAParquetFileUsingSchemaFromData.parquet");

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Write records to a parquet file");
        System.out.println("============================================================");

        DataReader reader = new CSVReader(new File("example/data/input/bank_account.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(new BasicFieldTransformer("Id").stringToInt())
                .add(new BasicFieldTransformer("Balance").stringToLong())
                .add(new BasicFieldTransformer("CreditLimit").stringToDouble())
                .add(new BasicFieldTransformer("AccountCreated").stringToDateTime("dd-mm-yyyy"))
                .add(new BasicFieldTransformer("Rating").stringToChar())
                ;

        reader = new DebugReader(reader);

        ParquetDataWriter writer = new ParquetDataWriter(PARQUET_FILE);
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
