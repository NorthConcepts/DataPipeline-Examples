/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.blog;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SelectFields;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class SelectivelyTransferCsvFile {

	public static void main(String[] args) {

		DataReader reader = new CSVReader(new File(
				"example/data/input/GeoliteDataInput.csv"))
				.setFieldNamesInFirstRow(false);

		TransformingReader transformingReader = new TransformingReader(reader);
		transformingReader.add(new SelectFields("E", "F"));

		DataWriter writer = new CSVWriter(new File(
				"example/data/output/GeoliteDataOutput.csv"))
				.setFieldNamesInFirstRow(false);

		Job.run(transformingReader, writer);

	}
}
