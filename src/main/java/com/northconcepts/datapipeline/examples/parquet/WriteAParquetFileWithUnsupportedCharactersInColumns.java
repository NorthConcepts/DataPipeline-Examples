package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DebugReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;
import com.northconcepts.datapipeline.transform.Transformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class WriteAParquetFileWithUnsupportedCharactersInColumns {

    private static final File PARQUET_FILE = new File("example/data/output/WriteAParquetFileWithSpacesInColumns.parquet");

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Write records to a parquet file");
        System.out.println("============================================================");

        DataReader reader = new CSVReader(new File("example/data/input/bank_account_spaces_in_column_names.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new DebugReader(reader);

        reader = new TransformingReader(reader)
                .add(new Transformer() {
                    @Override
                    public boolean transform(Record record) throws Throwable {
                        for (Field field : record) {
                            // Replace all unsupported symbols using regular expression with underscore in field name
                            field.setName(field.getName().replaceAll("[\\s=()\\[\\]{},]+", "_"));
                        }
                        return true;
                    }
                });

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
