package com.northconcepts.datapipeline.foundations.examples.jdbc;

import java.io.File;

import com.northconcepts.datapipeline.foundations.jdbc.JdbcConnection;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTable;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;

public class SortJdbcTablesTopologically {
    private static JdbcConnection jdbcConnection;

    public static void main(String[] args) throws Throwable {
        createConnection();

        jdbcConnection.loadTables();
        System.out.println("Tables are: ");
        for (JdbcTable jdbcTable : jdbcConnection.getTablesSortedTopologically()) {
            System.out.println(jdbcTable.getName());
        }
    }

    private static void createConnection() throws Throwable {
        jdbcConnection = new JdbcConnection()
                .setDriverClassName("org.h2.Driver")
                .setUsername("sa")
                .setName("H2_DB")
                .setPlainTextPassword("")
                .setUrl("jdbc:h2:mem:jdbcTableSort;MODE=MySQL");

        JdbcFacade jdbcfacade = new JdbcFacade(JdbcConnectionFactory.wrap(jdbcConnection.createConnection()));
        jdbcfacade.executeFile(new File("example/data/input/pre-sales.sql"));
    }
}
