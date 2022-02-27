/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.CachedLookup;
import com.northconcepts.datapipeline.transform.lookup.JdbcLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;
import com.northconcepts.datapipeline.transform.lookup.LookupTransformer;

public class CacheLookupValues {
    
    public static void main(String[] args) throws Throwable {
        // connect to the database
        Driver driver = (Driver) Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        Properties properties = new Properties();
        properties.put("user", "scott");
        properties.put("password", "tiger");
        Connection connection = driver.connect("jdbc:odbc:dp-cookbook", properties);

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
            .setFieldNamesInFirstRow(true);
        

        /* This lookup matches 
         *    [example/data/input/credit-balance-01.csv].[Rating] to [dp_rating].[rating_code]
         * to return    
         *    [dp_rating].[rating_description]
         */ 
        Lookup lookup = new JdbcLookup(connection, 
                "SELECT rating_decription FROM dp_rating WHERE rating_code=?");
        
        // cache JdbcLookup        
        lookup = new CachedLookup(lookup);   

        reader = new TransformingReader(reader)
                .add(new LookupTransformer(new FieldList("Rating"), lookup));
        
        Job.run(reader, new StreamWriter(System.out));
        
        connection.close();
    }
    
}
