/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
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
import com.northconcepts.datapipeline.jdbc.upsert.OracleUpsert;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class UpsertRecordsToOracle {

	public static void main(String[] args) throws Throwable {

		final String DATABASE_DRIVER = "oracle.jdbc.OracleDriver";
		final String DATABASE_URL = "jdbc:oracle:thin:system/oracle@localhost:1521:xe";
		final String DATABASE_USERNAME = "system";
		final String DATABASE_PASSWORD = "oracle";
		final String DATABASE_TABLE = "CreditBalance";
		final String PRIMARY_KEY = "Account";

		Class.forName(DATABASE_DRIVER);
		Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

		createTable(connection);

		DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02.csv")).setFieldNamesInFirstRow(true);
		reader = new TransformingReader(reader).add(
				new BasicFieldTransformer("Account").stringToInt(),
				new BasicFieldTransformer("FirstName"),
				new BasicFieldTransformer("LastName"),
				new BasicFieldTransformer("Balance").stringToFloat(),
				new BasicFieldTransformer("CreditLimit").stringToFloat(),
				new BasicFieldTransformer("AccountCreated").stringToDate("yyyy-MM-dd"),
				new BasicFieldTransformer("Rating").stringToChar());

		DataWriter writer = new JdbcUpsertWriter(connection, DATABASE_TABLE, new OracleUpsert(), PRIMARY_KEY);

		Job.run(reader, writer);

		reader = new JdbcReader(connection, "Select * from CreditBalance");
		writer = new CSVWriter(new OutputStreamWriter(System.out));

		Job.run(reader, writer);
	}

	public static void createTable(Connection connection) throws Throwable{
		PreparedStatement preparedStatement;
		String createTableQuery = "CREATE TABLE CreditBalance ("
			+ "Account NUMBER(10),"
			+ "LastName VARCHAR(256),"
			+ "FirstName VARCHAR(256),"
			+ "Balance NUMBER(19,4),"
			+ "CreditLimit NUMBER(19,4),"
			+ "AccountCreated DATE,"
			+ "Rating CHAR,"
			+ "PRIMARY KEY (Account)"
			+ ")";

		preparedStatement = connection.prepareStatement(createTableQuery);
		preparedStatement.execute();
	}
}
