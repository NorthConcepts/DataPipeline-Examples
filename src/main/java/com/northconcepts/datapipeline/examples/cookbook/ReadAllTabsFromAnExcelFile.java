/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import org.apache.log4j.Logger;

import java.io.File;

public class ReadAllTabsFromAnExcelFile {

    public static final Logger log = DataEndpoint.log;

    public static void main(String[] args) throws Throwable {
        ExcelDocument document = new ExcelDocument().open(new File("example/data/input/data-with-multiple-tabs.xlsx"));

        for (String sheetName : document.getSheetNames()) {
            DataReader reader = new ExcelReader(document).setFieldNamesInFirstRow(true).setSheetName(sheetName);
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
}
