/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.TeeReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingWriter;

public class WriteToADataWriterUsingTeeReader {
	
	private static final String INPUT = "example/data/input";
    private static final String OUTPUT = "example/data/output";

	public static void main(String[] args) throws Throwable {
		
		DataReader reader = new CSVReader(new File(INPUT, "purchases.csv"))
                .setFieldNamesInFirstRow(true);
		
		DataWriter writer1 = new CSVWriter(new File(OUTPUT, "purchases-total.csv"))
				.setFieldNamesInFirstRow(false);
		
		writer1 = new TransformingWriter(writer1)
				.add(new SelectFields("product_name", "total"));
		
		DataWriter writer2 = new CSVWriter(new File(OUTPUT, "purchases-details.csv"))
				.setFieldNamesInFirstRow(false);
		
		reader = new TeeReader(reader, writer1, true);
		Job job = Job.run(reader, writer2);

		System.out.println(job);
	}

}
