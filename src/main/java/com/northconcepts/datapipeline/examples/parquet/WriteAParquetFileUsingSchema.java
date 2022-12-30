package com.northconcepts.datapipeline.examples.parquet;

import java.io.File;
import java.sql.SQLException;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class WriteAParquetFileUsingSchema {

    public static void main(String[] args) throws SQLException {
        String schema = "message schema {" +
                "  optional int32 USER_ID (INTEGER(32,true));" +
                "  optional binary USER_NAME (STRING);" +
                "  optional binary FIRST_NAME (STRING);" +
                "  optional binary LAST_NAME (STRING);" +
                "  optional binary EMAIL (STRING);" +
                "  optional binary PASSWORD (STRING);" +
                "  optional int32 USER_ROLE_ID (INTEGER(32,true));" +
                "}";

        ParquetDataWriter writer = new ParquetDataWriter(new File("example/data/output/WriteAParquetFileUsingSchemaFromDatabase.parquet"));
        // Set schema using a record
        writer.setSchema(schema);

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
