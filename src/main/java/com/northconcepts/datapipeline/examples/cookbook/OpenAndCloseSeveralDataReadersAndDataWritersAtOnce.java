/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import com.northconcepts.datapipeline.core.DataEndpointGroup;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;

public class OpenAndCloseSeveralDataReadersAndDataWritersAtOnce {
    
    public static void main(String[] args) throws Throwable {
        // connect to the database
        Driver driver = (Driver) Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        Properties properties = new Properties();
        properties.put("user", "scott");
        properties.put("password", "tiger");
        Connection connection = driver.connect("jdbc:odbc:dp-cookbook", properties);

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        DataWriter writer = new  JdbcWriter(connection, "dp_credit_balance")
            .setAutoCloseConnection(true)
            .setBatchSize(100);
        
        DataEndpointGroup group = new DataEndpointGroup()
            .add(reader)
            .add(writer);

        group.open();
        try {
            Record record;
            while ((record = reader.read()) != null) {
                writer.write(record);
            }
        } finally {
            group.close();
        }
    }

}
