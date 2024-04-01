package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;
import com.northconcepts.datapipeline.foundations.time.DateTimePattern;

public class InferDateTimeFromCSVFile {
    public static void main(String[] args) {
        Pipeline pipeline = new Pipeline();
        pipeline.setInputAsDataReaderFactory(() -> new CSVReader(new File("example/data/input/DateTimeData.csv")).setFieldNamesInFirstRow(true));

        Dataset dataset = new MemoryDataset(pipeline);
        dataset.load().waitForColumnStatsToLoad();

        System.out.println("Column Count: " + dataset.getColumnCount());
        System.out.println("Record Count: " + dataset.getRecordCount());

        for (Column column : dataset.getColumns()) {
            System.out.println("===============================");
            System.out.println("Name: " + column.getName());
            System.out.println("Value Count: " + column.getValueCount());

            System.out.println("Is Temporal Column: " + column.isTemporal());
            System.out.println("    Inferred Temporal Value Count: " + column.getInferredTemporalValueCount());
            if (column.isTemporal()) {
                for (Map.Entry<DateTimePattern, LongAdder> entry : column.getTemporalPatterns().entrySet()) {
                    System.out.println("    " + entry.getKey().getPattern() + "  --  " + entry.getValue().longValue());
                }
            }

            System.out.println("Inferred Type Name: " + column.getInferredTypeName());
            System.out.println("Inferred Field Type: " + column.getInferredFieldType());
        }
    }
}
