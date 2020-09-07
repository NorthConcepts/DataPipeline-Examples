/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Endpoint;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.examples.cookbook.common.card.CardReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.CopyField;
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class MeasurePeformanceOfReaderAndWriter {

	public static void main(String[] args) {
		//set flag - captureElapsedTime to measure performance.
		Endpoint.setCaptureElapsedTime(true);
		
		measurePerformanceOfReader();
		measurePerformanceOfWriter();
	}

	private static void measurePerformanceOfReader() {
		DataReader reader = new CSVReader(new File("example/data/input/trades.csv"))
				.setFieldNamesInFirstRow(true);
		
		reader = new TransformingReader(reader)
				.add(new BasicFieldTransformer("price").stringToDouble())
				.add(new BasicFieldTransformer("time").stringToTime("hh:mm:s"));

		Job job = Job.run(reader, new NullWriter());

		System.out.println("Job Running Time:- " + job.getRunningTimeAsString());
		System.out.println("Total Records read:- " + job.getRecordsTransferred());
		
		System.out.println("Time taken by TranformingReader:- " + reader.getSelfTimeAsString());
		System.out.println("Time taken by CSVReader:- " + reader.getNestedEndpoint().getSelfTimeAsString());
	}

	private static void measurePerformanceOfWriter() {
		//Create a CardReader to retrieve records.
		DataReader reader = new CardReader()
				.setMaxRecords(1000000)
				.setStartingDate("2016-01-14 09:00:00.000")
				.setDateIncrement(500);
		
		DataWriter writer = new CSVWriter(new File("example/data/output/writer_performance_measurement.csv"));
		reader = new TransformingReader(reader)
				.add(new CopyField("card_value", "card_value2"))
				.add(new RenameField("card_number", "card_no"));
		
		Job job = Job.run(reader, writer);

		System.out.println("Job Running Time:- " + job.getRunningTimeAsString());
		System.out.println("Total Records:- " + job.getRecordsTransferred());
		
		System.out.println("Time taken by TranformingReader:- " + reader.getSelfTimeAsString());
		System.out.println("Time taken by CSVWriter:- " + reader.getNestedEndpoint().getSelfTimeAsString());

	}
}
