package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;

import java.io.File;

public class InferSourceDataTypes {
    public static void main(String[] args) {
        Pipeline pipeline = new Pipeline();
        pipeline.setInputAsDataReaderFactory(() -> new CSVReader(new File("example/data/input/mix-types.csv")).setFieldNamesInFirstRow(true));

        try (Dataset dataset = new MemoryDataset(pipeline)) {
            dataset.load().waitForColumnStatsToLoad();

            System.out.println("Column Count: " + dataset.getColumnCount());
            System.out.println("Record Count: " + dataset.getRecordCount());

            for (Column column : dataset.getColumns()) {
                System.out.println("===============================");
                System.out.println("Name: " + column.getName());
                System.out.println("Value Count: " + column.getValueCount());
                System.out.println("Sample Value: " + column.getSampleValue());
                System.out.println("Inferred Type Name: " + column.getInferredTypeName());
                System.out.println("Inferred Field Type: " + column.getBestFitInferredFieldType());
            }
        }
    }
}
