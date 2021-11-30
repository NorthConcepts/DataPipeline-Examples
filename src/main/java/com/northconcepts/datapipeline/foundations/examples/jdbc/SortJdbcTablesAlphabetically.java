package com.northconcepts.datapipeline.foundations.examples.jdbc;

import java.io.File;

import com.northconcepts.datapipeline.foundations.jdbc.JdbcConnection;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTable;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;

public class SortJdbcTablesAlphabetically {

    public static void main(String[] args) throws Throwable {
        JdbcConnection jdbcConnection = new JdbcConnection()
                .setDriverClassName("org.h2.Driver")
                .setUsername("sa")
                .setName("H2_DB")
                .setPlainTextPassword("")
                .setUrl("jdbc:h2:mem:jdbcTableSort;MODE=MySQL");

        JdbcFacade jdbcfacade = new JdbcFacade(JdbcConnectionFactory.wrap(jdbcConnection.createConnection()));
        jdbcfacade.executeFile(new File("example/data/input/pre-sales.sql"));

        jdbcConnection.loadTables(null, null, "%", "TABLE");
        System.out.println("Tables are: ");
        for (JdbcTable jdbcTable : jdbcConnection.getTablesSorted()) {
            System.out.println(jdbcTable.getName());
        }
    }
}
