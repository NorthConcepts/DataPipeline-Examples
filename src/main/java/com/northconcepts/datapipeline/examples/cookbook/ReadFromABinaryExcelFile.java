/*
 * Copyright (c) 2006-2025 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelDocument.ProviderType;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadFromABinaryExcelFile {

    public static void main(String[] args) throws Throwable {
        ExcelDocument document = new ExcelDocument(ProviderType.POI_XSSFB)
                .open(new File("example/data/input/employee_details.xlsb"));
        DataReader reader = new ExcelReader(document)
                                  //.setSheetName("employee_details")
                                  .setFieldNamesInFirstRow(true)
                                  .setAutoCloseDocument(true);

        
        Job.run(reader, StreamWriter.newSystemOutWriter());
    }
}
