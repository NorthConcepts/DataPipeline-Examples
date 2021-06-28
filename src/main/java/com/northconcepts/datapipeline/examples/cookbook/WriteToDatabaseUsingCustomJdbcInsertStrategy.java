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

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class WriteToDatabaseUsingCustomJdbcInsertStrategy {

	public static void main(String[] args) throws Throwable {

		final String DATABASE_DRIVER = "org.hsqldb.jdbcDriver";
		final String DATABASE_URL = "jdbc:hsqldb:mem:aname";
		final String DATABASE_USERNAME = "sa";
		final String DATABASE_PASSWORD = "";
		final String DATABASE_TABLE = "CreditBalance";

		Class.forName(DATABASE_DRIVER);
		Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

		createTable(connection);

		DataReader reader = new CSVReader(new File("example/data/input/credit-balance-insert-records.csv"))
							.setFieldNamesInFirstRow(true);
		reader = new TransformingReader(reader)
				.add(new BasicFieldTransformer("Account").stringToInt())
				.add(new BasicFieldTransformer("Balance").stringToDouble())
				.add(new BasicFieldTransformer("CreditLimit").stringToDouble())
				.add(new BasicFieldTransformer("Rating").stringToChar());
		
		DataWriter writer = new JdbcWriter(connection, DATABASE_TABLE, new CustomJdbcInsertStrategy());

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
			+ "Rating CHAR, "
			+ "PRIMARY KEY (Account));";

		preparedStatement = connection.prepareStatement(dropTableQuery);
		preparedStatement.execute();

		preparedStatement = connection.prepareStatement(createTableQuery);
		preparedStatement.execute();
	}
	
}
