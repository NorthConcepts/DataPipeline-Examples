package com.northconcepts.datapipeline.foundations.examples.jdbc;

import java.io.File;
import java.sql.Connection;

import com.northconcepts.datapipeline.foundations.jdbc.JdbcConnection;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTable;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;

public class SortJdbcTablesTopologically {
    private static JdbcConnection jdbcConnection;

    public static void main(String[] args) throws Throwable {
        createConnection();

        jdbcConnection.loadTables();
        for (JdbcTable jdbcTable : jdbcConnection.getTablesSortedTopologically()) {
            System.out.println(jdbcTable.getName());
        }
    }

    private static void createConnection() throws Throwable {
        Class.forName("org.hsqldb.jdbcDriver");

        jdbcConnection = new JdbcConnection()
                .setDriverClassName("org.hsqldb.jdbcDriver")
                .setUsername("sa")
                .setName("H2_DB")
                .setPlainTextPassword("")
                .setUrl("jdbc:hsqldb:mem:jdbcTableSort");

        Connection connection = jdbcConnection.createConnection();
        // createTableArtist(connection);

        JdbcFacade jdbcfacade = new JdbcFacade(JdbcConnectionFactory.wrap(connection));
        jdbcfacade.executeFile(new File("example/data/input/pre-sales.sql"));
    }
}
