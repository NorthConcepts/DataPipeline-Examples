/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Endpoint;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.CopyField;
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.TransformingWriter;

public class MeasurePerformanceOfReaderAndWriter {

	public static void main(String[] args) {
		//set flag - captureElapsedTime to measure performance.
		Endpoint.setCaptureElapsedTime(true);
		
		DataReader reader = new CSVReader(new File("example/data/input/trades.csv"))
				.setFieldNamesInFirstRow(true);
		
		reader = new TransformingReader(reader)
				.add(new BasicFieldTransformer("price").stringToDouble())
				.add(new BasicFieldTransformer("time").stringToTime("hh:mm:s"));

		DataWriter writer = new CSVWriter(new File("example/data/output/writer_performance_measurement.csv"));

		writer = new TransformingWriter(writer)
				.add(new CopyField("stock", "stock_name"))
				.add(new RenameField("shares", "share_count"));
		
		Job job = Job.run(reader, writer);

		System.out.println("Job Running Time:- " + job.getRunningTimeAsString());
		System.out.println("Total Records transferred:- " + job.getRecordsTransferred());
		
		System.out.println("Time taken by TranformingReader:- " + reader.getSelfTimeAsString());
		System.out.println("Time taken by CSVReader:- " + reader.getNestedEndpoint().getSelfTimeAsString());
		
		System.out.println("Time taken by TransformingWriter:- " + writer.getSelfTimeAsString());
		System.out.println("Time taken by CSVWriter:- " + writer.getNestedEndpoint().getSelfTimeAsString());
		
	}
	
}
