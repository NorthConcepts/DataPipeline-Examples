package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.AsyncReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class ReadACsvFileUsingMultithreading {
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        DataWriter writer = new JdbcWriter(getJdbcConnection(), "dp_credit_balance")
                .setAutoCloseConnection(true)
                .setBatchSize(100);

        reader = new AsyncReader(reader).setMaxBufferSizeInBytes(1024 * 1024 * 10);

        Job.run(reader, writer);
    }

    private static Connection getJdbcConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Driver driver = (Driver) Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        Properties properties = new Properties();
        properties.put("user", "scott");
        properties.put("password", "tiger");
        return driver.connect("jdbc:odbc:dp-cookbook", properties);
    }
}
