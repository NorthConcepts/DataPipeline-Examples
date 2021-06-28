/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.jdbc.JdbcReader;

public class ReadFromADatabase {

    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) throws Throwable {
        // connect to the database
        Driver driver = (Driver) Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        Properties properties = new Properties();
        properties.put("user", "scott");
        properties.put("password", "tiger");
        Connection connection = driver.connect("jdbc:odbc:dp-cookbook", properties);

        DataReader reader = new JdbcReader(connection, "SELECT * FROM dp_credit_balance")
            .setAutoCloseConnection(true);
        reader.open();
        try {
            Record record;
            while ((record = reader.read()) != null) {
                log.info(record);
            }
        } finally {
            reader.close();
        }
    }
}
