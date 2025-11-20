package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;

import java.util.List;

import static com.northconcepts.datapipeline.foundations.tools.DetectPrimaryKeysInDataset.detectPrimaryKeys;

public class DetectPrimaryKeysInADataset {
    public static void main(String[] args) {
        String[][] data = new String[][]{
                {"1", "1", "1", "1", "2", "1"},
                {"1", "2", "1", "2", "2", "1"},
                {"1", "3", "1", "3", "2", "2"},
                {"2", "1", "1", "4", "2", "2"},
                {"2", "2", "1", "5", "2", "3"},
                {"2", "3", "1", "6", "2", "4"},
        };

        RecordList records = new RecordList();
        for (int i = 0; i < data.length; i++) {
            String[] row = data[i];
            Record record = new Record();
            for (int j = 0; j < row.length; j++) {
                record.addField().setValue(row[j]);
            }
            records.add(record);
        }

        DataReader reader = new MemoryReader(records);
        DataWriter writer = new StreamWriter(System.out);

        Job.run(reader, writer);

        List<FieldList> primaryKeys = detectPrimaryKeys(records);
        if (primaryKeys.isEmpty()) {
            System.out.println("No primary keys found");
        } else {
            System.out.println("=================== Primary key column(s) ========================");
            primaryKeys.forEach(System.out::println);
        }
    }
}
