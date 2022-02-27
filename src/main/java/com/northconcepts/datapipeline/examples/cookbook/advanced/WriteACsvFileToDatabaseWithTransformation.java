/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.advanced;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.RemoveDuplicatesReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.RemoveFields;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class WriteACsvFileToDatabaseWithTransformation {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        
        // Use only the "Rating" and "CreditLimit" fields in duplicate test
        reader = new RemoveDuplicatesReader(reader, new FieldList("Rating", "CreditLimit"));        
        
        // Add AvailableCredit field, remove "CreditLimit", "Balance" fields
        reader = new TransformingReader(reader)
            .add(new SetCalculatedField("AvailableCredit", "parseDouble(CreditLimit) - parseDouble(Balance)"))
            .add(new RemoveFields("CreditLimit", "Balance"));
        
        DataWriter writer = new  JdbcWriter(getJdbcConnection(), "dp_credit_balance")
            .setAutoCloseConnection(true)
            .setBatchSize(100);
        
        Job.run(reader, writer);
    }
    
    private static Connection getJdbcConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Driver driver = (Driver) Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        Properties properties = new Properties();
        properties.put("user", "scott");
        properties.put("password", "tiger");
        Connection connection = driver.connect("jdbc:odbc:dp-cookbook", properties);
        return connection;
    }

}
