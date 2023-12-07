package com.northconcepts.datapipeline.foundations.examples.jdbc;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.jdbc.sql.select.Select;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class JoinCsvFiles {

    public static void main(String[] args) throws Throwable {

        Connection connection = JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:mem:jdbcTableSort;MODE=MySQL", "sa", "").createConnection();
        createCreditBalanceTable(connection);

        DataWriter writer = new JdbcWriter(connection, "CreditBalance");
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-insert-records2.csv")).setFieldNamesInFirstRow(true);
        Job job1 = Job.runAsync(reader, writer);


        connection = JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:mem:jdbcTableSort;MODE=MySQL", "sa", "").createConnection();
        createAccountTable(connection);

        writer = new JdbcWriter(JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:mem:jdbcTableSort;MODE=MySQL", "sa", ""), "Account");
        reader = new CSVReader(new File("example/data/input/user_account.csv")).setFieldNamesInFirstRow(true);
        reader = new TransformingReader(reader).add(new BasicFieldTransformer("DoB").stringToDate("yyyy-MM-dd"));
        Job job2 = Job.runAsync(reader, writer);

        job1.waitUntilFinished();
        job2.waitUntilFinished();

        Select select = new Select("CreditBalance")
            .select("CreditBalance.*", "Account.*")
            .leftJoin("Account", "CreditBalance.Account=Account.AccountNo");

        reader = new JdbcReader(connection, select.getSqlFragment());
        Job.run(reader, StreamWriter.newSystemOutWriter());
    }

    public static void createCreditBalanceTable(Connection connection) throws Throwable{
        PreparedStatement preparedStatement;
        String dropTableQuery = "DROP TABLE CreditBalance IF EXISTS;";
        String createTableQuery = "CREATE TABLE CreditBalance ("
            + "Account INTEGER, "
            + "Balance DOUBLE, "
            + "CreditLimit DOUBLE, "
            + "AccountCreated DATE,"
            + "Rating CHAR, "
            + "PRIMARY KEY (Account));";

        preparedStatement = connection.prepareStatement(dropTableQuery);
        preparedStatement.execute();

        preparedStatement = connection.prepareStatement(createTableQuery);
        preparedStatement.execute();
    }

    public static void createAccountTable(Connection connection) throws Throwable{
        PreparedStatement preparedStatement;
        String dropTableQuery = "DROP TABLE Account IF EXISTS;";
        String createTableQuery = "CREATE TABLE Account ("
            + "Id INTEGER, "
            + "AccountNo INTEGER, "
            + "LastName VARCHAR(256), "
            + "FirstName VARCHAR(256), "
            + "DoB DATE,"
            + "Country CHAR, "
            + "PRIMARY KEY (Id));";

        preparedStatement = connection.prepareStatement(dropTableQuery);
        preparedStatement.execute();

        preparedStatement = connection.prepareStatement(createTableQuery);
        preparedStatement.execute();
    }
}
