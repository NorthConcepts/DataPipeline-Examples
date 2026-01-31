package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelDocument.ProviderType;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadExcelFormattingAndStyles {

    public static void main(String[] args) throws Throwable {
        ExcelDocument document = new ExcelDocument(ProviderType.POI_XSSF_SAX)
                .open(new File("example/data/input/transactions-with-formatting-and-styles.xlsx"));
        DataReader reader = new ExcelReader(document)
                .setAutoCloseDocument(true)
                .setFieldNamesInFirstRow(true)
                .setReadMetadata(true); // only read excel cell styles if this flag is true

        // read from excel and write all excel records & cell styles on console.
        Job.run(reader, new StreamWriter(System.out).setFormat(StreamWriter.Format.RECORD_WITH_SESSION_PROPERTIES));
    }
}
