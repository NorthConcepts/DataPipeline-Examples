/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcUpsertWriter;
import com.northconcepts.datapipeline.jdbc.upsert.PostgreSqlUpsert;
import com.northconcepts.datapipeline.job.Job;

public class UpsertRecordsToPostgreSql {
    public static void main(String[] args) throws Throwable {
        final String DATABASE_DRIVER = "org.postgresql.Driver";
        final String DATABASE_URL = "jdbc:postgresql://localhost:5432";
        final String DATABASE_USERNAME = "test";
        final String DATABASE_PASSWORD = "test";
        final String DATABASE_TABLE = "CreditBalance";
        final String PRIMARY_KEY = "Account";

        Class.forName(DATABASE_DRIVER);
        Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

        createTable(connection);

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02.csv"))
                .setFieldNamesInFirstRow(true);
        DataWriter writer = new JdbcUpsertWriter(connection, DATABASE_TABLE, new PostgreSqlUpsert(), PRIMARY_KEY);

        Job.run(reader, writer);

        reader = new JdbcReader(connection, "Select * from CreditBalance;");
        writer = new CSVWriter(new OutputStreamWriter(System.out));

        Job.run(reader, writer);

    }

    public static void createTable(Connection connection) throws Throwable {
        PreparedStatement preparedStatement;
        String dropTableQuery = "DROP TABLE IF EXISTS CreditBalance";
        String createTableQuery = "CREATE TABLE CreditBalance (" 
                                    + "Account INTEGER, " 
                                    + "LastName VARCHAR(256), " 
                                    + "FirstName VARCHAR(256), "
                                    + "Balance double precision, " 
                                    + "CreditLimit double precision, " 
                                    + "AccountCreated DATE, " 
                                    + "Rating CHAR, "
                                    + "PRIMARY KEY (Account)"
                                    + ")";

        preparedStatement = connection.prepareStatement(dropTableQuery);
        preparedStatement.execute();

        preparedStatement = connection.prepareStatement(createTableQuery);
        preparedStatement.execute();
    }
}
