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

public class WriteAParquetFileUsingDatabaseTableSchema {

    private static final String PARQUET_FILE = "example/data/output/WriteAParquetFileUsingSchemaFromDatabase.parquet";

    public static void main(String[] args) throws Throwable {
        DB db = new DB(); // creates HSQL DB

        Connection connection = db.getConnection();

        db.executeFile(new File("example/data/input/user_information.sql"));

        DataReader reader = new JdbcReader(connection, "SELECT * FROM user").setAutoCloseConnection(false);

        ParquetDataWriter writer = new ParquetDataWriter(new File(PARQUET_FILE));

        // Set Parquet schema using a catalog, schema and table name with default JdbcValueReader.DEFAULT
        writer.setSchema(connection, null, "PUBLIC", "USER");

        // Set Parquet schema using a catalog, schema and table name with JdbcValueReader.STRICT
        //writer.setSchema(connection, JdbcValueReader.STRICT, null, "PUBLIC", "USER");

        System.out.println("===================Generated Parquet Schema========================");
        System.out.println(writer.getSchema());
        System.out.println("===================================================================");

        Job.run(reader, writer);

        System.out.println("=======================Reading Parquet File============================================");
        reader = new ParquetDataReader(new File(PARQUET_FILE));
        Job.run(reader, new StreamWriter(System.out));
    }

}
