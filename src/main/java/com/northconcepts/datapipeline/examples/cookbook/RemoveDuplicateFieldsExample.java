package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.RemoveDuplicateFields;
import com.northconcepts.datapipeline.transform.RemoveDuplicateFields.DuplicateFieldsPolicy;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class RemoveDuplicateFieldsExample {

	public static void main(String[] args) {
		retainAllDuplicateFields();
		retainFirstDuplicateFields();
		retainLastDuplicateFields();
		makeArrayOfDuplicateFields();
		renameDuplicateFields();
	}
	
	private static void retainAllDuplicateFields() {
		System.out.println("================================Retain All Duplicate Fields================================--");
		DataReader reader = new CSVReader(new File("example/data/input/duplicate_fields.csv"))
        		.setFieldNamesInFirstRow(true);
		
		reader = new TransformingReader(reader)
				.add(new RemoveDuplicateFields(DuplicateFieldsPolicy.RETAIN_ALL));
		
		Job.run(reader, new StreamWriter(System.out));
	}
	
	private static void retainFirstDuplicateFields() {
		System.out.println("\n\n================================Retain First field from Duplicate Fields================================");
		DataReader reader = new CSVReader(new File("example/data/input/duplicate_fields.csv"))
        		.setFieldNamesInFirstRow(true);
		
		reader = new TransformingReader(reader)
				.add(new RemoveDuplicateFields(DuplicateFieldsPolicy.RETAIN_FIRST));
		
		Job.run(reader, new StreamWriter(System.out));
	}
	
	private static void retainLastDuplicateFields() {
		System.out.println("\n\n================================Retain Last field from Duplicate Fields================================");
		DataReader reader = new CSVReader(new File("example/data/input/duplicate_fields.csv"))
        		.setFieldNamesInFirstRow(true);
		
		reader = new TransformingReader(reader)
				.add(new RemoveDuplicateFields(DuplicateFieldsPolicy.RETAIN_LAST));
		
		Job.run(reader, new StreamWriter(System.out));
	}
	
	private static void makeArrayOfDuplicateFields() {
		System.out.println("\n\n================================Create An Array Of All Duplicate Fields================================");
		DataReader reader = new CSVReader(new File("example/data/input/duplicate_fields.csv"))
        		.setFieldNamesInFirstRow(true);
		
		reader = new TransformingReader(reader)
				.add(new RemoveDuplicateFields(DuplicateFieldsPolicy.MAKE_ARRAY));
		
		Job.run(reader, new StreamWriter(System.out));
	}
	
	private static void renameDuplicateFields() {
		System.out.println("\n\n================================Rename Duplicate Fields================================");
		DataReader reader = new CSVReader(new File("example/data/input/duplicate_fields.csv"))
        		.setFieldNamesInFirstRow(true);
		
		reader = new TransformingReader(reader)
				.add(new RemoveDuplicateFields(DuplicateFieldsPolicy.RENAME));
		
		Job.run(reader, new StreamWriter(System.out));
	}
	
}
