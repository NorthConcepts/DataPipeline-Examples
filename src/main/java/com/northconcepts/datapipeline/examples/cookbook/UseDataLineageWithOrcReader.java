package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.lineage.FieldLineage;
import com.northconcepts.datapipeline.lineage.RecordLineage;
import com.northconcepts.datapipeline.orc.OrcDataReader;

public class UseDataLineageWithOrcReader {

    public static void main(String[] args) {
        DataReader reader = new OrcDataReader(new File("example/data/input/input_orc_file.orc"))
                                .setSaveLineage(true); // Enable lineage support. (By default, it is disabled.)
        Job.run(reader, new LineageWriter());
    }

    public final static class LineageWriter extends DataWriter {

        @Override
        protected void writeImpl(Record record) throws Throwable {
            System.out.println(record);

            RecordLineage recordLineage = new RecordLineage().setRecord(record);

            System.out.println("Record Lineage");
            System.out.println("    File: " + recordLineage.getFile());
            System.out.println("    Record: " + recordLineage.getRecordNumber());
            System.out.println("    Schema: " + recordLineage.getRecordSchema());
            System.out.println();

            FieldLineage fieldLineage = new FieldLineage();

            System.out.println("Field Lineage");
            for (int i = 0; i < record.getFieldCount(); i++) {
                Field field = record.getField(i);
                fieldLineage.setField(field);
                System.out.println("    " + field.getName());
                System.out.println("        File: " + fieldLineage.getFile());
                System.out.println("        Record: " + fieldLineage.getRecordNumber());
                System.out.println("        Field Index: " + fieldLineage.getOriginalFieldIndex());
                System.out.println("        Field Name: " + fieldLineage.getOriginalFieldName());
                System.out.println("        Field Schema: " + fieldLineage.getFieldSchema());
            }
            System.out.println("---------------------------------------------------------");
            System.out.println();
        }

    }
}
