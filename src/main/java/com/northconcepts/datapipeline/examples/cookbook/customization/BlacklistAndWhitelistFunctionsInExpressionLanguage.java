/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.customization;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Functions;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class BlacklistAndWhitelistFunctionsInExpressionLanguage {

	public static void main(String[] args) {
		callingBlacklistedFunctions();
		whitelistingBlacklistedFunctions();
		blacklistingCustomFunctions();
	}

	private static void callingBlacklistedFunctions() {
		System.out.println("================================Calling Blacklisted Functions================================--");
		try {
			DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
					.setFieldNamesInFirstRow(true);
	
			TransformingReader transformingReader = new TransformingReader(reader);
	
			//java.lang package is blacklisted by default and this will throw an exception
			transformingReader.add(new SetCalculatedField("currentTime", "java.lang.System.currentTimeMillis()"));
			
			Job.run(transformingReader, new StreamWriter(System.out));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static void whitelistingBlacklistedFunctions() {
		System.out.println("\n\n================================Whitelist Blacklisted Functions================================--");
		DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
				.setFieldNamesInFirstRow(true);

		TransformingReader transformingReader = new TransformingReader(reader);

		//Adding java.lang as whitelist.
		Functions.addWhitelistPrefix("java.lang.System.currentTimeMillis");
		
		transformingReader.add(new SetCalculatedField("currentTime", "java.lang.System.currentTimeMillis()"));
		
		Job.run(transformingReader, new StreamWriter(System.out));
	}
	
	private static void blacklistingCustomFunctions() {
		System.out.println("\n\n================================Blacklisting Custom Functions================================--");
		
		try {
			DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
					.setFieldNamesInFirstRow(true);
	
			TransformingReader transformingReader = new TransformingReader(reader);
	
			//Adding custom blacklist function
			Functions.addBlacklistPrefix("com.northconcepts.datapipeline.examples.cookbook.BlacklistAndWhitelistFunctionsInExpressionLanguage");
			
			//This will throw an exception.
			transformingReader.add(new SetCalculatedField("currentTime", "com.northconcepts.datapipeline.examples.cookbook.BlacklistAndWhitelistFunctionsInExpressionLanguage.getCurrentTime()"));
			
			Job.run(transformingReader, new StreamWriter(System.out));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}
}
