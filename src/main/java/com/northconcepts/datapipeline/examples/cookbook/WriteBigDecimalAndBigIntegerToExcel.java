/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class WriteBigDecimalAndBigIntegerToExcel {

	public static void main(String[] args) {
		Record record1 = new Record();
        record1.setField("name", "John Wayne");
        record1.setField("balance", new BigDecimal("1234567890.0987654321"));
        record1.setField("creditLimit", new BigInteger("98765432109876543210"));

        Record record2 = new Record();
        record2.setField("name", "Peter Parker");
        record2.setField("balance", new BigDecimal("987654321.123456789"));
        record2.setField("creditLimit", new BigInteger("12345678901234567890"));
        
        DataReader reader = new MemoryReader(new RecordList(record1, record2));
        
		ExcelDocument document = new ExcelDocument();
		DataWriter writer = new ExcelWriter(document).setSheetName("balance");

		Job.run(reader, writer);

		document.save(new File("example/data/output/WriteBigDecimalAndBigIntegerToExcel-credit-balance.xls"));
	}
}
