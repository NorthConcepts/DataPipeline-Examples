package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.sql.mysql.MySqlInsertWriter;

import java.io.File;

public class GenerateMySqlInsertStatementsFromCsvFile {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        DataWriter writer = new MySqlInsertWriter("credit-balance", new File("example/data/output/credit-balance-01.sql"))
                .setPretty(true) // Pretty print the SQL statements
                .setBatchSize(3); // Insert 3 records in a insert statement

        Job.run(reader, writer);
    }
}
