package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.jdbc.insert.OracleMultiRowInsertAllStatementInsert;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class OracleInsertMultiRowsUsingInsertAll {

    private static final String DATABASE_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String DATABASE_URL = "jdbc:oracle:thin:system/oracle@localhost:49161:xe";
    private static final String DATABASE_USERNAME = "system";
    private static final String DATABASE_PASSWORD = "oracle";
    private static final String DATABASE_TABLE = "CreditBalance";

    public static void main(String[] args) {
        Connection connection;

        try {
            Class.forName(DATABASE_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            createTable(connection);
        } catch (Throwable e) {
            throw DataException.wrap(e);
        }

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-insert-records.csv"))
                .setFieldNamesInFirstRow(true);
        reader = transform(reader);

        DataWriter writer = new JdbcWriter(connection, DATABASE_TABLE, new OracleMultiRowInsertAllStatementInsert())
                .setDebug(true) // log generated SQL
                .setBatchSize(3); // set batch size

        Job.run(reader, writer);

        reader = new JdbcReader(connection, "Select * from CreditBalance;");
        writer = new CSVWriter(new OutputStreamWriter(System.out));

        Job.run(reader, writer);
    }

    public static void createTable(Connection connection) throws Throwable{
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
    }

    public static DataReader transform(DataReader reader) {
        return new TransformingReader(reader).add(
                new BasicFieldTransformer("Account").stringToInt(),
                new BasicFieldTransformer("FirstName"),
                new BasicFieldTransformer("LastName"),
                new BasicFieldTransformer("Balance").stringToFloat(),
                new BasicFieldTransformer("CreditLimit").stringToFloat(),
                new BasicFieldTransformer("Rating").stringToChar());
    }
}
