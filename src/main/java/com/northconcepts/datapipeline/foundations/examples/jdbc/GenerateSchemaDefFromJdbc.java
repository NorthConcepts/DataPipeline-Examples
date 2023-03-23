package com.northconcepts.datapipeline.foundations.examples.jdbc;

import com.northconcepts.datapipeline.foundations.jdbc.JdbcConnection;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateSchemaFromJdbc;

import java.util.Arrays;

class GenerateSchemaDefFromJdbc {

    public static void main(String[] args) {
        JdbcConnection jdbcConnection = new JdbcConnection()
                .setDriverClassName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://127.0.0.1:3306")
                .setUsername("username")
                .setPlainTextPassword("password");

        GenerateSchemaFromJdbc generator = new GenerateSchemaFromJdbc(jdbcConnection);

        SchemaDef schema = generator.generate("databaseName", null, Arrays.asList("tableName1", "tableName2"), "TABLE");
        System.out.println(schema);
    }

}
