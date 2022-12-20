package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;
import java.sql.SQLException;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcValueReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class WriteAParquetFileUsingSchemaFromDatabase {

    public static void main(String[] args) throws SQLException {
        JdbcConnectionFactory jdbcConnectionFactory = JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:mem:jdbcTableSort;MODE=MySQL", "sa", "");

        JdbcFacade jdbcFacade = new JdbcFacade(jdbcConnectionFactory);
        jdbcFacade.executeFile(new File("example/data/input/user_information.sql"));

        ParquetDataWriter writer = new ParquetDataWriter(new File("example/data/output/WriteAParquetFileUsingSchemaFromDatabase.parquet"));
        // Set schema using a query
        // writer.setSchema(jdbcConnectionFactory, "SELECT * FROM user");

        // Set schema using a query with parameters
        //writer.setSchema(jdbcConnectionFactory, "SELECT * FROM user WHERE user_role_id=?", 1);

        // Set schema using a query with parameters & JdbcValueReader (default is OPINIONATED)
        writer.setSchema(jdbcConnectionFactory, JdbcValueReader.STRICT, "SELECT * FROM user WHERE user_role_id=?", 1);

        System.out.println("===================Generated Parquet Schema========================");
        System.out.println(writer.getSchema());
        System.out.println("===================================================================");

        // Reading records from table user_information and writing to parquet file.
        DataReader reader = new JdbcReader(jdbcConnectionFactory.createConnection(), "select * from user").setAutoCloseConnection(true);
        Job.run(reader, writer);

        System.out.println("=======================Reading Parquet File============================================");
        reader = new ParquetDataReader(new File("example/data/output/WriteAParquetFileUsingSchemaFromDatabase.parquet"));
        Job.run(reader, new StreamWriter(System.out));
    }
}
