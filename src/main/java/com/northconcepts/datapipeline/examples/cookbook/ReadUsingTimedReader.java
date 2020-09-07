/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.util.concurrent.TimeUnit;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.core.TimedReader;
import com.northconcepts.datapipeline.examples.cookbook.common.card.CardReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadUsingTimedReader {

	public static void main(String[] args) {
		//Create a CardReader to retrieve records with specified delay in read.
		DataReader reader = new CardReader()
        		.setMaxRecords(10000)
        		.setReadPeriod(TimeUnit.SECONDS.toMillis(1))
				.setStartingDate("2016-01-14 09:00:00.000")
				.setDateIncrement(500);
		
        // Use TimedReader to read for limited time.
        reader = new TimedReader(reader, TimeUnit.SECONDS.toMillis(10)); // Read only for 10 seconds.
        
        Job job = Job.run(reader, new NullWriter());
        
        System.out.println("Total Records retrieved :- " + job.getRecordsTransferred());
        System.out.println("Running Time in milli seconds :- " + job.getRunningTimeAsString());
        
	}
}
