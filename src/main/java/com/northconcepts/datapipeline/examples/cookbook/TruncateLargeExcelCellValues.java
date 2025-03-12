/*
 * Copyright (c) 2006-2025 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelDocument.ProviderType;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.excel.LargeCellHandler;
import com.northconcepts.datapipeline.job.Job;

public class TruncateLargeExcelCellValues {

    private static final String INPUT_FILE_PATH = "data/input/csv_with_large_cell_value.csv";
    private static final String OUTPUT_EXCEL_FILE_PATH = "data/output/truncate-large-excel-cell-values.xlsx";
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File(INPUT_FILE_PATH))
                .setFieldNamesInFirstRow(true);
        
        ExcelDocument document = new ExcelDocument(ProviderType.POI_SXSSF);
        DataWriter writer = new ExcelWriter(document)
                                  .setFieldNamesInFirstRow(true)
                                  // The default is LargeCellHandler.FAIL
                                  // Other possible options are TRUNCATE & SKIP
                                  .setLargeCellHandler(LargeCellHandler.TRUNCATE);

        Job.run(reader, writer);
        document.save(new File(OUTPUT_EXCEL_FILE_PATH));
        
        readExcelAndPrintToConsole();
    }
    
    private static void readExcelAndPrintToConsole() {
        ExcelDocument document = new ExcelDocument(ProviderType.POI_XSSF_SAX)
                .open(new File(OUTPUT_EXCEL_FILE_PATH));
        DataReader reader = new ExcelReader(document)
                                  .setFieldNamesInFirstRow(true)
                                  .setAutoCloseDocument(true);
        
        Job.run(reader, StreamWriter.newSystemOutWriter());
    }
}
