package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;
import java.sql.Connection;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.examples.database.DB;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class WriteAParquetFileUsingSchemaFromDatabaseQuery {

    private static final String PARQUET_FILE = "example/data/output/WriteAParquetFileUsingSchemaFromDatabaseQuery.parquet";

    public static void main(String[] args) throws Throwable {
        DB db = new DB(); // creates HSQL DB

        Connection connection = db.getConnection();

        db.executeFile(new File("example/data/input/user_information.sql"));

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

}
