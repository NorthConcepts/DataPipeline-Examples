package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.io.FileWriter;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.sql.postgresql.PostgreSqlUpsertWriter;

public class WritePostgreSqlUpsertStatementsToAFile {

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        DataWriter writer = new PostgreSqlUpsertWriter("credit_balance",
                new FileWriter("example/data/output/credit-balance-upsert-postgresql.sql"), "Account")
                .setPretty(true);

        Job.run(reader, writer);
    }

}
