package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class WriteAParquetFileUsingSchemaFromDatabase {

    private static final String PARQUET_FILE = "example/data/output/WriteAParquetFileUsingSchemaFromDatabase.parquet";

    public static void main(String[] args) throws Throwable {
        Connection connection = openConnection();

        // Create table in database.
        JdbcConnectionFactory factory = JdbcConnectionFactory.wrap(connection);
        JdbcFacade client = new JdbcFacade(factory);
        client.executeFile(new File("example/data/input/user_information.sql"));

        DataReader reader = new JdbcReader(connection, "SELECT * FROM user").setAutoCloseConnection(true);

        ParquetDataWriter writer = new ParquetDataWriter(new File(PARQUET_FILE));

        // Set Parquet schema using a query
        writer.setSchema(connection, "SELECT * FROM user");

        // Set Parquet schema using a query with parameters
        //writer.setSchema(connection, "SELECT * FROM user WHERE user_role_id=?", 1);

        // Set Parquet schema using a query with parameters & JdbcValueReader (default is OPINIONATED)
        // writer.setSchema(connection, JdbcValueReader.STRICT, "SELECT * FROM user WHERE user_role_id=?", 1);

        System.out.println("===================Generated Parquet Schema========================");
        System.out.println(writer.getSchema());
        System.out.println("===================================================================");

        Job.run(reader, writer);

        System.out.println("=======================Reading Parquet File============================================");
        reader = new ParquetDataReader(new File(PARQUET_FILE));
        Job.run(reader, new StreamWriter(System.out));
    }

    private static Connection openConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Driver driver = (Driver) Class.forName("org.h2.Driver").newInstance();
        Properties properties = new Properties();
        properties.put("user", "sa");
        properties.put("password", "");
        Connection connection = driver.connect("jdbc:h2:mem:jdbcTableSort;MODE=MySQL", properties);
        return connection;
    }

}
