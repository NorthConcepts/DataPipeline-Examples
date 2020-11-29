package com.northconcepts.datapipeline.examples.cookbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.lineage.FieldLineage;
import com.northconcepts.datapipeline.lineage.RecordLineage;

public class UseDataLineageWithJdbcReader {

    public static void main(String[] args) throws Throwable {
        
        final String DATABASE_DRIVER = "org.hsqldb.jdbcDriver";
        final String DATABASE_URL = "jdbc:hsqldb:mem:aname";
        final String DATABASE_USERNAME = "sa";
        final String DATABASE_PASSWORD = "";
        final String DATABASE_TABLE = "CreditBalance";

        Class.forName(DATABASE_DRIVER);
        Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

        createTable(connection);
        
        DataReader reader = new JdbcReader(connection, "SELECT * FROM " + DATABASE_TABLE) 
                                .setSaveLineage(true);
        
        Job.run(reader, new LineageWriter());
    }
    
    public static void createTable(Connection connection) throws Throwable{
        PreparedStatement preparedStatement;
        String dropTableQuery = "DROP TABLE CreditBalance IF EXISTS;";

        preparedStatement = connection.prepareStatement(dropTableQuery);
        preparedStatement.execute();
        
        String createTableQuery = "CREATE TABLE CreditBalance ("
                + "Id INTEGER, "
                + "Name VARCHAR(256), "
                + "Balance DOUBLE, "
                + "CreditLimit DOUBLE, "
                + "PRIMARY KEY (Id));";

        preparedStatement = connection.prepareStatement(createTableQuery);
        preparedStatement.execute();
        
        String insertDataQuery = "INSERT INTO CreditBalance values"
                + "(1, 'Harry Potter', 20000, 100000), "
                + "(2, 'Harmione', 1500, 1000), "
                + "(3, 'Dumbledore', 5000, 100000) ";
        
        preparedStatement = connection.prepareStatement(insertDataQuery);
        preparedStatement.execute();
    }
    
    public final static class LineageWriter extends DataWriter {

        @Override
        protected void writeImpl(Record record) throws Throwable {
            System.out.println(record);
            
            RecordLineage recordLineage = new RecordLineage().setRecord(record);
            
            System.out.println("Record Session Properties: ");
            System.out.println("Record Number: " + recordLineage.getRecordNumber());
            System.out.println("Query String: " + recordLineage.getQueryString());
            
            System.out.println();
            
            FieldLineage fieldLineage = new FieldLineage();
            
            System.out.println("Field Session Properties: ");
            for (int i=0; i < record.getFieldCount(); i++) {
                Field field = record.getField(i);
                fieldLineage.setField(field);
                System.out.println("Field Index: " + fieldLineage.getFieldIndex());
                System.out.println("Field Name: " + fieldLineage.getFieldName());
                System.out.println("Column Name: " + fieldLineage.getColumnName());
                System.out.println("Query String: " + recordLineage.getQueryString());
            }
            System.out.println("---------------------------------------------------------");
            System.out.println();
        }
        
    }
}
