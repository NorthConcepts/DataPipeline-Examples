package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.jdbc.sql.SqlPart;
import com.northconcepts.datapipeline.sql.mysql.*;

public class GenerateMySqlDdlProgrammatically {
    public static void main(String[] args) {
        MySqlPart createUsersTable = new CreateTable("employee")
                .addColumn(new CreateTableColumn("id", TableColumnType.INT).setAutoIncrement(true).setPrimaryKey(true))
                .addColumn(new CreateTableColumn("name", TableColumnType.VARCHAR).setNullable(false))
                .addColumn(new CreateTableColumn("email", TableColumnType.VARCHAR).setUnique(true))
                .addColumn(new CreateTableColumn("departmentId", TableColumnType.INT))
                .setPretty(true);

        SqlPart createAddressTable = new CreateTable("department")
                .addColumn(new CreateTableColumn("id", TableColumnType.INT).setAutoIncrement(true).setPrimaryKey(true))
                .addColumn(new CreateTableColumn("name", TableColumnType.VARCHAR))
                .addColumn(new CreateTableColumn("description", TableColumnType.TEXT))
                .addColumn(new CreateTableColumn("managerId", TableColumnType.INT))
                .setPretty(true);

        SqlPart addDepartmentIdForeignKey = new CreateForeignKey("fk_employee_department", "department", "employee")
                .setForeignKeyColumnNames("departmentId")
                .setPrimaryKeyColumnNames("id")
                .setOnDeleteAction(ForeignKeyAction.SET_NULL)
                .setPretty(true);

        SqlPart addManagerIdForeignKey = new CreateForeignKey("fk_department_manager", "employee", "department")
                .setForeignKeyColumnNames("managerId")
                .setPrimaryKeyColumnNames("id")
                .setPretty(true);

        SqlPart addIndex = new CreateIndex("ix_employee_name", "employee", "name");

        System.out.println(createUsersTable.getSqlFragment());
        System.out.println(createAddressTable.getSqlFragment());
        System.out.println(addDepartmentIdForeignKey.getSqlFragment());
        System.out.println(addManagerIdForeignKey.getSqlFragment());
        System.out.println(addIndex.getSqlFragment());
    }
}
