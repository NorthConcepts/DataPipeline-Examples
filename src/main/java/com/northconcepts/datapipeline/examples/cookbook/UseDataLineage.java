package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.lineage.FieldLineage;
import com.northconcepts.datapipeline.lineage.RecordLineage;

public class UseDataLineage {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/rating-table-01.csv"))
                .setFieldNamesInFirstRow(true)
                .setSaveLineage(true); // Enable lineage support. (By default, it is disabled.) 
                                          // If lineage is not supported by reader, exception will be thrown.
        
        Job.run(reader, new LineageWriter());
    }
    
    public final static class LineageWriter extends DataWriter {

        @Override
        protected void writeImpl(Record record) throws Throwable {
            System.out.println(record);
            
            RecordLineage recordLineage = new RecordLineage().setRecord(record);
            
            System.out.println("Record Session Properties: ");
            System.out.println("Record Number: " + recordLineage.getRecordNumber());
            System.out.println("File Name: " + recordLineage.getFile());
            System.out.println("Line Number: " + recordLineage.getLineNumber());
            System.out.println("Column Number: " + recordLineage.getColumnNumber());
            
            System.out.println();
            
            FieldLineage fieldLineage = new FieldLineage();
            
            System.out.println("Field Session Properties: ");
            for (int i=0; i < record.getFieldCount(); i++) {
                Field field = record.getField(i);
                fieldLineage.setField(field);
                System.out.println("Field Index: " + fieldLineage.getFieldIndex());
                System.out.println("Field Name: " + fieldLineage.getFieldName());
            }
            System.out.println("---------------------------------------------------------");
            System.out.println();
        }
        
    }
}
