package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.lineage.FieldLineage;
import com.northconcepts.datapipeline.lineage.RecordLineage;

public class UseDataLineageWithExcelReader {
    public static void main(String[] args) {
        ExcelDocument document = new ExcelDocument().open(new File("example/data/input/credit-balance-01.xls"));
        DataReader reader = new ExcelReader(document)
                .setSheetName("credit-balance")
                .setFieldNamesInFirstRow(true)
                .setSaveLineage(true);

        Job.run(reader, new LineageWriter());
    }

    public final static class LineageWriter extends DataWriter {

        @Override
        protected void writeImpl(Record record) throws Throwable {
            System.out.println(record);

            RecordLineage recordLineage = new RecordLineage().setRecord(record);

            System.out.println("Record Lineage");
            System.out.println("    File: " + recordLineage.getFile());
            System.out.println("    File Line: " + recordLineage.getFileLineNumber());
            System.out.println("    File Column: " + recordLineage.getFileColumnNumber());
            System.out.println("    Record: " + recordLineage.getRecordNumber());

            System.out.println();

            FieldLineage fieldLineage = new FieldLineage();

            System.out.println("Field Lineage");
            for (int i = 0; i < record.getFieldCount(); i++) {
                Field field = record.getField(i);
                fieldLineage.setField(field);
                System.out.println("    " + field.getName());
                System.out.println("        File: " + fieldLineage.getFile());
                System.out.println("        File Line: " + fieldLineage.getFileLineNumber());
                System.out.println("        File Column: " + fieldLineage.getFileColumnNumber());
                System.out.println("        Record: " + fieldLineage.getRecordNumber());
                System.out.println("        Field Index: " + fieldLineage.getOriginalFieldIndex());
                System.out.println("        Field Name: " + fieldLineage.getOriginalFieldName());
            }
            System.out.println("---------------------------------------------------------");
            System.out.println();
        }

    }
}
