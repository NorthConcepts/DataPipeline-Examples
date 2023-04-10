package com.northconcepts.datapipeline;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcMultiUpsertWriter;
import com.northconcepts.datapipeline.jdbc.upsert.MySqlUpsert;
import com.northconcepts.datapipeline.job.Job;

import javax.sql.DataSource;
import java.io.File;

public class UpsertWithMultipleJdbcConnections {

    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "datapipeline"; // create this database if not exists.
    private static final String DATABASE_USERNAME = "etl";
    private static final String DATABASE_PASSWORD = "etl";
    private static final String DATABASE_TABLE = "CreditBalance";
    private static final String PRIMARY_KEY = "Account";

    public static void main(String[] args) throws Throwable {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setServerName(SERVER_NAME);
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setUser(DATABASE_USERNAME);
        dataSource.setPassword(DATABASE_PASSWORD);

        createTable(dataSource);

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02.csv")).setFieldNamesInFirstRow(true);
        // Write to a database using 3 connections.
        DataWriter writer = new JdbcMultiUpsertWriter(dataSource, 3, 1,  DATABASE_TABLE, new MySqlUpsert(), PRIMARY_KEY);

        Job.run(reader, writer);
    }

    public static void createTable(DataSource dataSource) {
        JdbcFacade jdbcFacade = new JdbcFacade(JdbcConnectionFactory.wrap(dataSource));
        String dropTableQuery = "DROP TABLE IF EXISTS CreditBalance;";
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

        jdbcFacade.execute(dropTableQuery);
        jdbcFacade.execute(createTableQuery);

        jdbcFacade.close();
    }
}
