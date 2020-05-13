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
            for(JdbcTableColumn column : table.getColumns()) {
                createColumnClass(column, table);
            }
        }

        System.out.println(code.getSource());
    }

    public static void createTableClass(JdbcTable table) {
        sourceWriter.println("public class %s {", table.getName());
        sourceWriter.println();
        sourceWriter.indent();
        sourceWriter.println("private Set<Column> columns = new HashSet<>();");
        sourceWriter.println();
        sourceWriter.println("public Set<Column> getColumns() { return columns; }");
        sourceWriter.println();
        sourceWriter.println("public %s setColumns(Set<Column> columns) {", table.getName());
        sourceWriter.indent();
        sourceWriter.println("this.columns = columns;");
        sourceWriter.println("return this;");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();
        sourceWriter.println("public %s add(Column column) {", table.getName());
        sourceWriter.indent();
        sourceWriter.println("columns.add(column);");
        sourceWriter.println("return this;");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();
    }

    public static void createColumnClass(JdbcTableColumn column, JdbcTable table) {
        sourceWriter.println("public class %s {", table.getName() + column.getName());
        sourceWriter.println();
        sourceWriter.indent();
        sourceWriter.println("private String name;");
        sourceWriter.println("private String type;");
        sourceWriter.println();
        sourceWriter.println("public String getName() { return name; }");
        sourceWriter.println();
        sourceWriter.println("public %s setName(String name) {", column.getName());
        sourceWriter.indent();
        sourceWriter.println("this.name = name;");
        sourceWriter.println("return this;");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();
        sourceWriter.println("public String getType() {", column.getName());
        sourceWriter.indent();
        sourceWriter.println("return \"%s\";", column.getMethodSuffix());
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();
    }
}
