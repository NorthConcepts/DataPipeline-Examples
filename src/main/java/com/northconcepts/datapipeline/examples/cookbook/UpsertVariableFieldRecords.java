package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcUpsertWriter;
import com.northconcepts.datapipeline.jdbc.upsert.GenericUpsert;
import com.northconcepts.datapipeline.jdbc.upsert.IUpsert;
import com.northconcepts.datapipeline.jdbc.upsert.VariableFieldsUpsert;
import com.northconcepts.datapipeline.job.Job;

public class UpsertVariableFieldRecords {

	public static void main(String[] args) throws Throwable {

		final String DATABASE_DRIVER = "org.hsqldb.jdbcDriver";
		final String DATABASE_URL = "jdbc:hsqldb:mem:aname";
		final String DATABASE_USERNAME = "sa";
		final String DATABASE_PASSWORD = "";
		final String DATABASE_TABLE = "CreditBalance";
		final String PRIMARY_KEY = "Account";

		Class.forName(DATABASE_DRIVER);
		Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

		createTable(connection);

		VariableFieldsUpsert upsert = new VariableFieldsUpsert(new VariableFieldsUpsert.IUpsertFactory() {
			@Override
			public IUpsert createUpsert(JdbcUpsertWriter writer, Record record) {
				return new GenericUpsert().setInsertFirst(false);
			}
		}).setDebug(true);

		DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02.csv")).setFieldNamesInFirstRow(true);
		DataWriter writer = new JdbcUpsertWriter(connection, DATABASE_TABLE, upsert, PRIMARY_KEY);

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
