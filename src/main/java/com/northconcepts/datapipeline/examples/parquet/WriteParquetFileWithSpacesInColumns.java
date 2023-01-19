package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DebugReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class WriteParquetFileWithSpacesInColumns {

    private static final File PARQUET_FILE = new File("example/data/output/WriteParquetFileWithSpacesInColumns.parquet");

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("Write records to a parquet file");
        System.out.println("============================================================");

        DataReader reader = new CSVReader(new File("example/data/input/bank_account_spaces_in_column_names.csv"))
                .setFieldNamesInFirstRow(true);

        // Option 1: If there are limited and known fields, then use TranformingReader to rename field name.
        //        reader = new TransformingReader(reader)
        //                .add(new RenameField("Account No", "Account_No"))
        //                .add(new RenameField("Last Name", "Last_Name"))
        //                .add(new RenameField("First Name", "First_Name"))
        //                .add(new RenameField("Credit Limit", "Credit_Limit"))
        //                .add(new RenameField("Account Created", "Account_Created"))
        //                ;

        // Option 2: Change all field name with regular expression.
        reader = new ProxyReader(reader) {

            private List<String> targetFieldNames;

            @Override
            protected Record interceptRecord(Record record) throws Throwable {
                if (targetFieldNames == null) {
                    targetFieldNames = new ArrayList<>(record.getFieldCount());
                    for (Field field: record) {
                        targetFieldNames.add(field.getName().replaceAll("(\s+)", "_")); // Replace all the whitespace characters with underscore.
                    }
                }

                for (int i = 0; i < targetFieldNames.size(); i++) {
                    record.getField(i).setName(targetFieldNames.get(i));
                }

                return record;
            }
        };

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
