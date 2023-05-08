package com.northconcepts.datapipeline.examples.parquet;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.examples.database.DB;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

import java.io.File;
import java.sql.Connection;

public class WriteDatabaseViewToParquetFile {

    private static final String PARQUET_FILE = "example/data/output/WriteAParquetFileUsingSchemaFromDatabaseView.parquet";

    public static void main(String[] args) {
        DB db = new DB(); // creates HSQL DB

        Connection connection = db.getConnection();

        db.executeFile(new File("example/data/input/user_information.sql"));
        db.executeFile(new File("example/data/input/user_information_view.sql"));
        
        DataReader reader = new JdbcReader(connection, "SELECT * FROM users_with_lastname_as_doe")
                .setAutoCloseConnection(false);

        ParquetDataWriter writer = new ParquetDataWriter(new File(PARQUET_FILE));
        writer.setSchema(connection, null, "PUBLIC", "USERS_WITH_LASTNAME_AS_DOE");

        System.out.println("===================Generated Parquet Schema========================");
        System.out.println(writer.getSchema());
        System.out.println("===================================================================");

        Job.run(reader, writer);

        System.out.println("=======================Reading Parquet File============================================");
        reader = new ParquetDataReader(new File(PARQUET_FILE));
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
