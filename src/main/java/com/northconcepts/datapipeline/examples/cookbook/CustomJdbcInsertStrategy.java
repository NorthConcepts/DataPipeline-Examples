/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.sql.PreparedStatement;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.jdbc.insert.IInsert;
import com.northconcepts.datapipeline.jdbc.sql.insert.Insert;

public class CustomJdbcInsertStrategy implements IInsert {

    private PreparedStatement insertStatement = null;
    private boolean debug;

    @Override
    public void addBatch() throws Throwable {
        insertStatement.addBatch();
    }

    @Override
    public DataException addExceptionProperties(DataException exception) {
        return exception;
    }

    @Override
    public void close() throws Throwable {
        insertStatement.close();
    }

    @Override
    public IInsert clone() {
        return new CustomJdbcInsertStrategy()
                .setDebug(debug);
    }

    @Override
    public void executeBatch() throws Throwable {
        throw new DataException("Batch is not supported");
    }

    @Override
    public void executeUpdate() throws Throwable {
        insertStatement.executeUpdate();
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

    @Override
    public void prepare(JdbcWriter writer, Record record) throws Throwable {

        Insert insert = new Insert(writer.getTableName());
        for (int i = 0; i < record.getFieldCount(); i++) {
            insert.add(record.getField(i).getName());
        }
        String insertSql = insert.getSqlFragment();

        if (debug) {
            System.out.println("SQL Statement:- " + insertSql);
        }

        insertStatement = writer.getConnection().prepareStatement(insertSql);
    }

    @Override
    public void setValues(JdbcWriter writer, Record record) throws Throwable {
        insertStatement.clearParameters();
        for (int i = 0; i < record.getFieldCount(); i++) {
            Field field = record.getField(i);
            writer.setParameterValue(field, insertStatement, i + 1);
        }
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public CustomJdbcInsertStrategy setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

}
