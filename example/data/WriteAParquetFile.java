package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;
import java.time.LocalDate;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DebugReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class WriteAParquetFile {

    private static final File PARQUET_FILE = new File("example/data/input/date_output.parquet");

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Write records to a parquet file");
        System.out.println("============================================================");
        
        DataReader reader = new CSVReader(new File("example/data/input/date_input.csv"))
                .setFieldNamesInFirstRow(true);

        LocalDate date = LocalDate.of(2021, 01, 01);
        
        reader = new TransformingReader(reader)
                .add(new BasicFieldTransformer("update_date").stringToDate("dd-MM-yyyy"))
//                .add(new SetField("update_date", date))
//                .add(new SetField("source_file", "testFile.csv"))
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
