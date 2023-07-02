package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelDocument.ProviderType;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.job.Job;

public class UseStreamingExcelReading {
    
    public static void main(String[] args) {
        ExcelDocument document = new ExcelDocument(ProviderType.POI_XSSF_SAX)
                .open(new File("example/data/input/call-center-inbound-call.xlsx"));
        DataReader reader = new ExcelReader(document)
            .setFieldNamesInFirstRow(true);

        Job.run(reader, new StreamWriter(System.out)); // prints 100 records
    }

}
