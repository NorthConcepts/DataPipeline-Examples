package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.jdbc.insert.OracleMultiRowInsertAllStatementInsert;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InsertMultipleRowsToOracleUsingInsertAll {

    private static final String DATABASE_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String DATABASE_URL = "jdbc:oracle:thin:system/oracle@localhost:1521:xe";
    private static final String DATABASE_USERNAME = "system";
    private static final String DATABASE_PASSWORD = "oracle";
    private static final String DATABASE_TABLE = "CreditBalance";

    public static void main(String[] args) throws Throwable {
        Class.forName(DATABASE_DRIVER);

        Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        createTable(connection);

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-insert-records.csv"))
                .setFieldNamesInFirstRow(true);
        DataWriter writer = new JdbcWriter(connection, DATABASE_TABLE, new OracleMultiRowInsertAllStatementInsert())
                .setDebug(true) // log generated SQL
                .setBatchSize(3); // set batch size

        Job.run(reader, writer);

        connection.close();
    }

    public static void createTable(Connection connection) throws Throwable {
        PreparedStatement preparedStatement;
        String createTableQuery = "CREATE TABLE CreditBalance ("
                + "Account INTEGER, "
                + "LastName VARCHAR(256), "
                + "FirstName VARCHAR(256), "
                + "Balance DOUBLE, "
                + "CreditLimit DOUBLE, "
                + "Rating CHAR, "
                + "PRIMARY KEY (Account));";

        preparedStatement = connection.prepareStatement(createTableQuery);
        preparedStatement.execute();

        preparedStatement.close();
    }
}
