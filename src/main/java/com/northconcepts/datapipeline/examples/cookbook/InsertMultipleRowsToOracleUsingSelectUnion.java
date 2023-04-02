/*
 * Copyright (c) 2006-2023 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential. Use is subject to license terms.
 *
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.jdbc.insert.OracleMultiRowSelectUnionAllStatementInsert;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InsertMultipleRowsToOracleUsingSelectUnion {

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
        reader = transform(reader);

        DataWriter writer = new JdbcWriter(connection, DATABASE_TABLE, new OracleMultiRowSelectUnionAllStatementInsert())
                .setDebug(true) // log generated SQL
                .setBatchSize(3); // set batch size

        Job.run(reader, writer);

        connection.close();
    }

    public static void createTable(Connection connection) throws Throwable {
        PreparedStatement preparedStatement;
        String createTableQuery = "CREATE TABLE CreditBalance ("
                + "Account NUMBER(10),"
                + "LastName VARCHAR(256),"
                + "FirstName VARCHAR(256),"
                + "Balance NUMBER(19,4),"
                + "CreditLimit NUMBER(19,4),"
                + "Rating CHAR,"
                + "PRIMARY KEY (Account)"
                + ")";

        preparedStatement = connection.prepareStatement(createTableQuery);
        preparedStatement.execute();

        preparedStatement.close();
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
