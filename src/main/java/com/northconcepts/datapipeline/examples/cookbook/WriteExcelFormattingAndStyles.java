package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.FieldLocationPredicate;
import com.northconcepts.datapipeline.excel.ExcelCellStyleDecorator;
import com.northconcepts.datapipeline.excel.ExcelColorPalette;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonRecordReader;

public class WriteExcelFormattingAndStyles {

    public static void main(String[] args) {
        DataReader reader = new JsonRecordReader(new File("example/data/input/transactions_data.json"))
                .addRecordBreak("/array/object");
        
        ExcelDocument document = new ExcelDocument();
        ExcelWriter writer = new ExcelWriter(document)
                .setSheetName("transactions")
                .setAutofitColumns(true);
        
        // Add Formatting & Cell Style for Header
        writer.addHeaderCellStyle(FieldLocationPredicate.all(), ExcelCellStyleDecorator.bold()
                .and(ExcelCellStyleDecorator.backgroundColor(ExcelColorPalette.DARK_YELLOW))
                .and(ExcelCellStyleDecorator.fontColor(ExcelColorPalette.LIGHT_GREEN))
                );

        // Add Formatting & Cell Style for Data
        writer.addDataCellStyle(FieldLocationPredicate.negativeNumber(), ExcelCellStyleDecorator.fontColor(ExcelColorPalette.RED)
                .and(ExcelCellStyleDecorator.italic()));
        writer.addDataCellStyle(FieldLocationPredicate.evenRowIndex(), ExcelCellStyleDecorator.backgroundColor(ExcelColorPalette.GREY_25_PERCENT));

        Job.run(reader, writer);
        
        document.save(new File("example/data/output/transactions-with-formatting-and-styles.xlsx"));
    }

}
