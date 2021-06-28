/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.jdbc;

import com.northconcepts.datapipeline.foundations.jdbc.JdbcConnection;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTable;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTableColumn;
import com.northconcepts.datapipeline.foundations.sourcecode.CodeWriter;
import com.northconcepts.datapipeline.foundations.sourcecode.JavaCodeBuilder;


public class GenerateJavaBeansFromADatabase {

    static JavaCodeBuilder code = new JavaCodeBuilder();
    static CodeWriter sourceWriter = code.getSourceWriter();

    public static void main(String... args) {

        JdbcConnection connection = new JdbcConnection()
                .setDriverClassName("org.postgresql.Driver")
                .setUrl("jdbc:postgresql://localhost:5432/customers")
                .setUsername("username")
                .setPlainTextPassword("password");


        for(JdbcTable table : connection.loadTables().getTables()) {
            createTableClass(table);
        }

        System.out.println(code.getSource());
    }

    public static void createTableClass(JdbcTable table) {
        sourceWriter.println("public class %s {", table.getName());
        sourceWriter.indent();
        sourceWriter.println();
        for(JdbcTableColumn column : table.getColumns()) {
            sourceWriter.println("private %s %s;", column.getMethodSuffix(), column.getName());
        }
        sourceWriter.println();
        for(JdbcTableColumn column : table.getColumns()) {
            sourceWriter.println("public %s set%s(%s %s){", table.getName(), column.getName(),
                    column.getMethodSuffix(), column.getName());
            sourceWriter.indent();
            sourceWriter.println("this.%s = %s;", column.getName(), column.getName());
            sourceWriter.println("return this;");
            sourceWriter.outdent();
            sourceWriter.println("}");
            sourceWriter.println();
            sourceWriter.println("public %s get%s(){", column.getMethodSuffix(),column.getName());
            sourceWriter.indent();
            sourceWriter.println("return %s;", column.getName());
            sourceWriter.outdent();
            sourceWriter.println("}");
            sourceWriter.println();
        }
        sourceWriter.println("public %s read(ResultSet resultSet) {", table.getName());
        sourceWriter.indent();
        for(JdbcTableColumn column : table.getColumns()) {
            sourceWriter.println("%s = resultSet.get%s(\"%s\");", column.getName(), column.getMethodSuffix(),
                    column.getName());
        }
        sourceWriter.println("return this;");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();
    }
}
