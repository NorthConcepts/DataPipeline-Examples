/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.twitter;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.MultiWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.CopyField;
import com.northconcepts.datapipeline.transform.MoveFieldAfter;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.twitter.ApiLimitPolicy;
import com.northconcepts.datapipeline.twitter.TwitterSearchReader;

public class SearchTwitterForTweets {
    
    public static final Logger log = DataEndpoint.log;
    
    private static final String QUERY = "#Java";
    private static final int MAX_RESULTS = 500;
    private static final String CONSUMER_KEY = "*********";  // Your Twitter API Consumer Key
    private static final String CONSUMER_SECRET = "***********";  // Your Twitter API Consumer Secret

    public static void main(String[] args) {
        

        
        
        // Read from Twitter
        DataReader reader = new TwitterSearchReader(CONSUMER_KEY, CONSUMER_SECRET, QUERY, MAX_RESULTS)
            .setApiLimitPolicy(ApiLimitPolicy.STOP);
        
        // Split CreatedAt into CreatedAtDate and CreatedAtTime
        reader = new TransformingReader(reader)
            .add(new CopyField("CreatedAt", "CreatedAtDate", false))
            .add(new CopyField("CreatedAt", "CreatedAtTime", false))
            .add(new BasicFieldTransformer("CreatedAtDate").dateTimeToDate())
            .add(new BasicFieldTransformer("CreatedAtTime").dateTimeToTime())
            .add(new MoveFieldAfter("CreatedAtDate", "CreatedAt"))
            .add(new MoveFieldAfter("CreatedAtTime", "CreatedAtDate"));
      
      // Write to console  
      DataWriter writer1 = new StreamWriter(System.out);
      
      // Write to Excel  
      ExcelDocument document = new ExcelDocument();
      DataWriter writer2 = new ExcelWriter(document).setSheetName("search");

      // Write to both console and Excel  
      MultiWriter writer = new MultiWriter(writer1, writer2);
      
      // Run job, writing to
      Job.run(reader, writer); 

      
      // Save Excel file  
      document.save(new File("example/data/output/twitter-search.xlsx"));
    }

}
