package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.jdbc.sql.SqlPart;
import com.northconcepts.datapipeline.sql.postgresql.*;

public class GeneratePostgreSqlDdlProgrammatically {
    public static void main(String[] args) {
        SqlPart createUsersTable = new CreateTable("users")
                .addColumn(new CreateTableColumn("id", TableColumnType.SERIAL).setPrimaryKey(true))
                .addColumn(new CreateTableColumn("name", TableColumnType.VARCHAR).setNullable(false))
                .addColumn(new CreateTableColumn("email", TableColumnType.VARCHAR).setUnique(true))
                .addColumn(new CreateTableColumn("addressId", TableColumnType.INT))
                .setPretty(true);

        SqlPart createAddressTable = new CreateTable("address")
                .addColumn(new CreateTableColumn("id", TableColumnType.SERIAL).setPrimaryKey(true))
                .addColumn(new CreateTableColumn("address", TableColumnType.VARCHAR))
                .addColumn(new CreateTableColumn("city", TableColumnType.VARCHAR))
                .addColumn(new CreateTableColumn("state", TableColumnType.VARCHAR))
                .addColumn(new CreateTableColumn("country", TableColumnType.VARCHAR).setLength(3))
                .setPretty(true);

        SqlPart addForeignKey = new CreateForeignKey("fk_users_address", "address", "users")
                .setForeignKeyColumnNames("addressId")
                .setPrimaryKeyColumnNames("id")
                .setOnDeleteAction(ForeignKeyAction.CASCADE)
                .setPretty(true);

        SqlPart addIndex = new CreateIndex("ix_users_name", "users", "name");

        System.out.println(createUsersTable.getSqlFragment());
        System.out.println(createAddressTable.getSqlFragment());
        System.out.println(addForeignKey.getSqlFragment());
        System.out.println(addIndex.getSqlFragment());
    }
}
