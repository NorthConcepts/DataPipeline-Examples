/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
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

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcUpsertWriter;
import com.northconcepts.datapipeline.jdbc.upsert.MergeUpsert;
import com.northconcepts.datapipeline.job.Job;

public class WriteToDatabaseUsingMergeUpsertWithBatch {

	public static void main(String[] args) {

		final String DATABASE_DRIVER = "org.hsqldb.jdbcDriver";
		final String DATABASE_URL = "jdbc:hsqldb:mem:aname";
		final String DATABASE_USERNAME = "sa";
		final String DATABASE_PASSWORD = "";
		final String DATABASE_TABLE = "CreditBalance";
		final String PRIMARY_KEY = "Account";

		Connection connection;
		try {
			Class.forName(DATABASE_DRIVER);
			connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

			createTable(connection);
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}

		DataReader reader = new CSVReader(new File("example/data/input/credit-balance-upsert-records.csv")).setFieldNamesInFirstRow(true);
		DataWriter writer = new JdbcUpsertWriter(connection, DATABASE_TABLE, new MergeUpsert().setDebug(true), PRIMARY_KEY)
				.setBatchSize(100);

		Job.run(reader, writer);

		reader = new JdbcReader(connection, "Select * from CreditBalance;");
		writer = new CSVWriter(new OutputStreamWriter(System.out));

		Job.run(reader, writer);
	}

	public static void createTable(Connection connection) throws Throwable{
		PreparedStatement preparedStatement;
		String dropTableQuery = "DROP TABLE CreditBalance IF EXISTS;";
		String createTableQuery = "CREATE TABLE CreditBalance ("
			+ "Account INTEGER, "
			+ "LastName VARCHAR(256), "
			+ "FirstName VARCHAR(256), "
			+ "Balance DOUBLE, "
			+ "CreditLimit DOUBLE, "
			+ "AccountCreated DATE, "
			+ "Rating CHAR, "
			+ "PRIMARY KEY (Account));";

		preparedStatement = connection.prepareStatement(dropTableQuery);
		preparedStatement.execute();

		preparedStatement = connection.prepareStatement(createTableQuery);
		preparedStatement.execute();
	}
}
