/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class ReadBigDecimalAndBigIntegerFromAnExcelFile {

    public static void main(String[] args) throws Throwable {
        ExcelDocument document = new ExcelDocument()
        		.open(new File("example/data/input/ReadBigDecimalAndBigIntegerToExcel-credit-balance.xls"));
        DataReader reader = new ExcelReader(document)
            .setSheetName("balance")
            .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
                .add(new BasicFieldTransformer("balance").stringToBigDecimal()) // convert balance to BigDecimal
                .add(new BasicFieldTransformer("creditLimit").stringToBigInteger()); //convert creditLimit to BigInteger
        
        Job.run(reader, new StreamWriter(System.out));
        
    }
}
