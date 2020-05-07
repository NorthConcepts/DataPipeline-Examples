package com.northconcepts.datapipeline.foundations.examples.jdbc;

import com.northconcepts.datapipeline.foundations.jdbc.JdbcConnection;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTable;
import com.northconcepts.datapipeline.foundations.jdbc.JdbcTableColumn;
import com.northconcepts.datapipeline.internal.lang.Util;

import java.util.LinkedHashSet;
import java.util.Set;

public class GenerateJavaBeansFromADatabase {

    public static void main(String... args) {

        JdbcConnection connection = new JdbcConnection()
                .setDriverClassName("org.postgresql.Driver")
                .setUrl("jdbc:postgresql://localhost:5432/customers")
                .setUsername("username")
                .setPlainTextPassword("password");

        for(JdbcTable table : connection.loadTables().getTables()) {

            Catalog catalog = new Catalog();
            Schema schema = new Schema();
            Table dbTable = new Table();

            if(!Util.isEmpty(table.getCatalogName())) {
                catalog.getTables().add(dbTable);
            }

            if(!Util.isEmpty(table.getSchemaName())) {
                schema.getTables().add(dbTable);
            }

            dbTable.setName(table.getName());

            for(JdbcTableColumn column : table.getColumns()) {
                dbTable.getColumns().add(new Column()
                                            .setName(column.getName())
                                            .setType(column.getJavaType()));
            }
        }
    }

    public static class Table {

        private Set<Column> columns = new LinkedHashSet<>();
        private String name;

        public Table(){}

        public Set<Column> getColumns() {
            return columns;
        }

        public Table setColumns(Set<Column> columns) {
            this.columns = columns;
            return this;
        }

        public String getName() {
            return name;
        }

        public Table setName(String name) {
            this.name = name;
            return this;
        }
    }

    public static class Column {
            private String name;
            private Class<?> type;

        public String getName() {
            return name;
        }

        public Column setName(String name) {
            this.name = name;
            return this;
        }

        public Class<?> getType() {
            return type;
        }

        public Column setType(Class<?> type) {
            this.type = type;
            return this;
        }
    }

    public static class Catalog {

        private String name;

        private Set<Table> tables;

        public Catalog(){}

        public String getName() {
            return name;
        }

        public Catalog setName(String name) {
            this.name = name;
            return this;
        }

        public Set<Table> getTables() {
            return tables;
        }

        public void setTables(Set<Table> tables) {
            this.tables = tables;
        }
    }

    public static class Schema{

        private String name;

        private Set<Table> tables;

        public Schema(){}

        public String getName() {
            return name;
        }

        public Schema setName(String name) {
            this.name = name;
            return this;
        }

        public Set<Table> getTables() {
            return tables;
        }

        public void setTables(Set<Table> tables) {
            this.tables = tables;
        }
    }
}
