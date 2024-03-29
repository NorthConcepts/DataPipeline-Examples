package com.northconcepts.datapipeline.examples.cookbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.internal.test.DataPipelineUnitTest;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadUuidFromPostgreSQL extends DataPipelineUnitTest {

    private final static String DATABASE_DRIVER = "org.postgresql.Driver";
    private final static String DATABASE_URL = "jdbc:postgresql://localhost:5432/contacts?stringtype=unspecified";
    private final static String DATABASE_USERNAME = "test";
    private final static String DATABASE_PASSWORD = "test";
    
    public static void main(String[] args) throws Throwable {
        Class.forName(DATABASE_DRIVER);
        Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        try {
            createTableAndInsertRecords(connection);
            
            JdbcReader reader = new JdbcReader(connection, "SELECT * FROM contacts");
            DataWriter writer = StreamWriter.newSystemOutWriter();
            
            Job.run(reader, writer);
        } finally {
            connection.close();
        }
    }
    
    public static void createTableAndInsertRecords(Connection connection) throws Throwable{
        PreparedStatement preparedStatement;
        String dropTableQuery = "DROP TABLE IF EXISTS contacts;";
        
        String createTableQuery = "CREATE TABLE contacts ("
                + "    contact_id uuid,"
                + "    first_name VARCHAR NOT NULL,"
                + "    last_name VARCHAR NOT NULL,"
                + "    email VARCHAR NOT NULL,"
                + "    phone VARCHAR,"
                + "    employee_uuid uuid NULL,"
                + "    PRIMARY KEY (contact_id)"
                + ");";
        
        String insertRecords = "INSERT INTO contacts (contact_id, first_name, last_name, email, phone, employee_uuid) "
                + "VALUES "
                + "  ('54e2037c-13c5-42fd-af89-97facd8bfdb8', 'John', 'Smith', 'john.smith@example.com',  '408-237-2345', '07cc0695-195d-4ba5-ade5-830c90cbfe80'), "
                + "  ('03199f44-943e-49c3-9f56-b876eaeabba5', 'Jane', 'Smith', 'jane.smith@example.com', '408-237-2344', null), "
                + "  ('59465728-9401-4a3c-8d08-2a3b324715bc', 'Alex', 'Smith', 'alex.smith@example.com', '408-237-2343', 'e9d29c97-dc83-41f3-bef4-aa3782edb85d');";

        preparedStatement = connection.prepareStatement(dropTableQuery);
        preparedStatement.execute();
        preparedStatement.close();

        preparedStatement = connection.prepareStatement(createTableQuery);
        preparedStatement.execute();
        preparedStatement.close();
        
        preparedStatement = connection.prepareStatement(insertRecords);
        preparedStatement.execute();
        preparedStatement.close();
    }
}
