/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;

public class ReadFromAnExcelFile {

    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) throws Throwable {
        ExcelDocument document = new ExcelDocument().open(new File("example/data/input/credit-balance-01.xls"));
        DataReader reader = new ExcelReader(document)
            .setSheetName("credit-balance")
            .setFieldNamesInFirstRow(true);

        reader.open();
        try {
            Record record;
            while ((record = reader.read()) != null) {
                log.info(record);
            }
        } finally {
            reader.close();
        }
    }
}