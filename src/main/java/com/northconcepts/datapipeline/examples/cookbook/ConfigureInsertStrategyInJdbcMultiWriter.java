package com.northconcepts.datapipeline.examples.cookbook;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcMultiWriter;
import com.northconcepts.datapipeline.jdbc.insert.MySqlPreparedStatementInsert;
import com.northconcepts.datapipeline.job.Job;

import javax.sql.DataSource;
import java.io.File;

public class ConfigureInsertStrategyInJdbcMultiWriter {

    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "datapipeline";
    private static final String DATABASE_USERNAME = "etl";
    private static final String DATABASE_PASSWORD = "etl";
    private static final String DATABASE_TABLE = "inbound_calls";

    public static void main(String[] args) {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setServerName(SERVER_NAME);
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setUser(DATABASE_USERNAME);
        dataSource.setPassword(DATABASE_PASSWORD);

        createTable(dataSource);

        ExcelDocument doc = new ExcelDocument().open(new File("example/data/input/call-center-inbound-call-2.xlsx"));
        DataReader reader = new ExcelReader(doc).setFieldNamesInFirstRow(true);

        // Configure MySqlPreparedStatementInsert strategy
        DataWriter writer = new JdbcMultiWriter(dataSource, 5, 10, DATABASE_TABLE, new MySqlPreparedStatementInsert().setDebug(true))
                .setBatchSize(10); // set batch size

        Job.run(reader, writer);
    }

    public static void createTable(DataSource dataSource) {
        JdbcFacade jdbcFacade = new JdbcFacade(JdbcConnectionFactory.wrap(dataSource));
        String dropTableQuery = "DROP TABLE IF EXISTS inbound_calls;";
        String createTableQuery = "CREATE TABLE inbound_calls ("
                + "id INTEGER, "
                + "event_type VARCHAR(45), "
                + "agent_id INTEGER, "
                + "phone_number VARCHAR(45), "
                + "start_time VARCHAR(45), "
                + "end_time VARCHAR(45), "
                + "disposition VARCHAR(45));";

        jdbcFacade.execute(dropTableQuery);
        jdbcFacade.execute(createTableQuery);

        jdbcFacade.close();
    }
}
