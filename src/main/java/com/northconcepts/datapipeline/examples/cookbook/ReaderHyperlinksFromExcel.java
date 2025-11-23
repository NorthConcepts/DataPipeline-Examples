package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.ProxyWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelDocument.ProviderType;
import com.northconcepts.datapipeline.excel.ExcelFieldMetadata;
import com.northconcepts.datapipeline.excel.ExcelHyperlink;
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

        DataWriter writer = new CSVWriter(OUTPUT_FILE);
        writer = new CsvProxyWriter(writer);

        Job.run(reader, writer);
    }
}

class CsvProxyWriter extends ProxyWriter {

    private ExcelFieldMetadata metadata = new ExcelFieldMetadata();

    public CsvProxyWriter(DataWriter nestedDataWriter) {
        super(nestedDataWriter);
    }

    @Override
    protected Record interceptRecord(Record record) throws Throwable {
        for (int i = 0; i < record.getFields().size(); i++) {
            Field field = record.getFields().get(i);
            metadata.setField(field);

            ExcelHyperlink excelHyperlink = metadata.getExcelHyperlink();

            if (excelHyperlink != null) {
                record.addField("product_link", excelHyperlink.getLocation());
            }
        }
        return super.interceptRecord(record);
    }

}
