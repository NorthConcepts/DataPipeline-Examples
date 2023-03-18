package com.northconcepts.datapipeline.foundations.examples.jdbc;

import com.northconcepts.datapipeline.foundations.jdbc.JdbcConnection;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateSchemaFromJdbc;

import java.util.ArrayList;
import java.util.List;

class GenerateSchemaDefFromJdbc {

    public static void main(String[] args) {
        JdbcConnection jdbcConnection = new JdbcConnection()
                .setDriverClassName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://127.0.0.1:3306")
                .setUsername("root")
                .setPlainTextPassword("root");

        GenerateSchemaFromJdbc generator = new GenerateSchemaFromJdbc(jdbcConnection);
        
        List<String> tableNames = new ArrayList<>();
        tableNames.add("tableName");
        tableNames.add("tableName");

        SchemaDef schema = generator.generate("databaseName", null, tableNames, "TABLE");
        System.out.println(schema);
    }

}
