package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;
import java.sql.SQLException;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class WriteAParquetFileUsingSchemaFromRecord {

    public static void main(String[] args) throws SQLException {
        Record record = new Record()
                .setField("USER_ID", 11)
                .setField("USER_NAME", "james_bond")
                .setField("FIRST_NAME", "james")
                .setField("LAST_NAME", "bond")
                .setField("EMAIL", "james_bond@example.com")
                .setField("PASSWORD", "*******************")
                .setField("USER_ROLE_ID", 1)
                ;

        ParquetDataWriter writer = new ParquetDataWriter(new File("example/data/output/WriteAParquetFileUsingSchemaFromDatabase.parquet"));
        // Set schema using a record
        writer.setSchema(record);

        System.out.println("===================Generated Parquet Schema========================");
        System.out.println(writer.getSchema());
        System.out.println("===================================================================");

        JdbcConnectionFactory jdbcConnectionFactory = JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:mem:jdbcTableSort;MODE=MySQL", "sa", "");

        JdbcFacade jdbcFacade = new JdbcFacade(jdbcConnectionFactory);
        jdbcFacade.executeFile(new File("example/data/input/user_information.sql"));

        // Reading records from table user and writing to parquet file.
        DataReader reader = new JdbcReader(jdbcConnectionFactory.createConnection(), "select * from user").setAutoCloseConnection(true);
        Job.run(reader, writer);

        System.out.println("=======================Reading Parquet File============================================");
        reader = new ParquetDataReader(new File("example/data/output/WriteAParquetFileUsingSchemaFromDatabase.parquet"));
        Job.run(reader, new StreamWriter(System.out));
    }
}
