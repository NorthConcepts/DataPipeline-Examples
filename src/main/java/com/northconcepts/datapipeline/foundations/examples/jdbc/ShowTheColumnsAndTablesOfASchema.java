/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.jdbc;

import com.northconcepts.datapipeline.foundations.jdbc.JdbcConnection;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcExportedKey;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcImportedKey;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTable;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTableColumn;

import java.util.List;
import java.util.stream.Collectors;

public class ShowTheColumnsAndTablesOfASchema {

    public static void main(String[] args) {

        JdbcConnection connection = new JdbcConnection()
                .setDriverClassName("org.postgresql.Driver")
                .setUrl("jdbc:postgresql://localhost:5432/customers")
                .setUsername("username")
                .setPlainTextPassword("password");

        List<JdbcTable> tables = connection.loadTables(null, null, "%", "TABLE").getTables();

        for (JdbcTable table : tables) {
            System.out.println(table.getName() + " : " + table.getType());
            System.out.println("    Columns");
            for (JdbcTableColumn column : table.getColumns()) {
                System.out.println("    - " + column.getName() + " : " + column.getDatabaseTypeName() + "(" + column.getSize() + ")"
                        + (column.isNullable()?"":" NOT NULL")
                        + (column.isPrimaryKey()?" (PK " + column.getPrimaryKeySequence() + ")":"")
                        + (column.isAutoIncrement()?" (AUTO)":"")
                        + (column.isGenerated()?" (GEN)":"")
                        + (column.isPseudo()?" (Pseudo)":"")
                        + (column.isRowIdentifier()?" (RowID " + column.getRowIdentifierSequence() + ")":""));
            }

            System.out.println("    ImportedKeys");
            for (JdbcImportedKey key : table.getImportedKeys()) {
                System.out.println("    --> " + key.getForiegnKeyName() + " (" + table.getImportedKeyColumns(key.getForiegnKeyName()).stream().map(JdbcTableColumn::getName).collect(Collectors.joining(",")) + ")");
            }

            System.out.println("    ExportedKeys");
            for (JdbcExportedKey key : table.getExportedKeys()) {
                System.out.println("    --> " + key.getForiegnKeyName() + " (" + key.getForiegnKeyTableName() + "." + key.getForiegnKeyColumnName()+ ")");
            }

            System.out.println("-------------------------------");

        }

        System.out.println("Done.");
    }
}
