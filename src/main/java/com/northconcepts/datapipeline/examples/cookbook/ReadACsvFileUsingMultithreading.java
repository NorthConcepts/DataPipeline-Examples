package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.AsyncReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.examples.database.DB;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;
import java.sql.Connection;

public class ReadACsvFileUsingMultithreading {
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);
        reader = new AsyncReader(reader).setMaxBufferSizeInBytes(1024 * 1024 * 10);

        DB db = new DB();
        db.execute("CREATE TABLE credit_balance (" +
                " account INT NOT NULL," +
                " lastname VARCHAR(32)," +
                " firstname VARCHAR(32)," +
                " balance DECIMAL," +
                " creditlimit DECIMAL," +
                " accountcreated  VARCHAR(12)," +
                " rating VARCHAR(5)," +
                " PRIMARY KEY (account));");

        Connection connection = db.getConnection();
        DataWriter writer = new JdbcWriter(connection, "credit_balance");

        Job.run(reader, writer);
    }
}
