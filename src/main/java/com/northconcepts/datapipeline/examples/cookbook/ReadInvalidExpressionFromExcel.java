package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelDocument.ProviderType;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.excel.ExcelReader.FailedExpressionStrategy;
import com.northconcepts.datapipeline.job.Job;

public class ReadInvalidExpressionFromExcel {

    public static void main(String[] args) throws Throwable {
        ExcelDocument document = new ExcelDocument(ProviderType.POI_XSSF_SAX)
                .open(new File("example/data/input", "excel_with_invalid_expression.xlsx"));
        DataReader reader = new ExcelReader(document)
                .setAutoCloseDocument(true)
                .setFieldNamesInFirstRow(true)
                .setFailedExpressionStrategy(FailedExpressionStrategy.SET_EXCEPTION_MESSAGE)
                ;

        // read from excel and write all excel records on console.
        Job.run(reader, new StreamWriter(System.out));
    }
}
