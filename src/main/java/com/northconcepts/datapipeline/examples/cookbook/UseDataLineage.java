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
            
            System.out.println("Record Session Properties: ");
            System.out.println("Record Number: " + record.getSessionProperty(RecordLineage.RECORD_NUMBER));
            System.out.println("File Name: " + record.getSessionProperty(RecordLineage.FILE));
            System.out.println("Line Number: " + record.getSessionProperty(RecordLineage.LINE_NUMBER));
            System.out.println("Column Number: " + record.getSessionProperty(RecordLineage.RECORD_NUMBER));
            
            System.out.println();
            
            System.out.println("Field Session Properties: ");
            for (int i=0; i < record.getFieldCount(); i++) {
                Field field = record.getField(i);
                System.out.println("Field Index: " + field.getSessionProperty(FieldLineage.FIELD_INDEX));
                System.out.println("Field Name: " + field.getSessionProperty(FieldLineage.FIELD_NAME));
            }
            System.out.println("---------------------------------------------------------");
            System.out.println();
        }
        
    }
}
