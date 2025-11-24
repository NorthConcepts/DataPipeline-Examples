package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelDocument.ProviderType;
import com.northconcepts.datapipeline.excel.ExcelFieldMetadata;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.job.Job;

public class ReaderHyperlinksFromExcel {

    private static File INPUT_FILE = new File("example/data/input", "product_list.xlsx");
    private static File OUTPUT_FILE = new File("example/data/output", "product_list.csv");

    public static void main(String[] args) {
        ExcelDocument document = new ExcelDocument(ProviderType.POI_XSSF_SAX)
                .open(INPUT_FILE);
        DataReader reader = new ExcelReader(document)
                .setFieldNamesInFirstRow(true)
                .setAutoCloseDocument(true)
                .setReadMetadata(true);

        reader = new ExcelHyperlinkReader(reader);

        DataWriter writer = new CSVWriter(OUTPUT_FILE);

        Job.run(reader, writer);
    }
}

class ExcelHyperlinkReader extends ProxyReader {

    private ExcelFieldMetadata metadata = new ExcelFieldMetadata();

    public ExcelHyperlinkReader(DataReader dataReader) {
        super(dataReader);
    }

    @Override
    protected Record interceptRecord(Record record) throws Throwable {
        Field field = record.getField("product_name");
        metadata.setField(field);
        
        record.addField("product_link", metadata.getExcelHyperlink() == null ? null : metadata.getExcelHyperlink().getLocation());
        
        return super.interceptRecord(record);
    }

}
