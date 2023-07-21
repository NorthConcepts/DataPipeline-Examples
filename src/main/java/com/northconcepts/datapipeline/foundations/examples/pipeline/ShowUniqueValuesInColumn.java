package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;

public class ShowUniqueValuesInColumn {

    public static void main(String[] args) {

        Pipeline pipeline = new Pipeline();

        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFileSource().setPath("data/input/Listing.csv"))
                .setFieldNamesInFirstRow(true);

        pipeline.setInput(pipelineInput);

        Dataset dataset = new MemoryDataset(pipeline).setCollectUniqueValues(true); //enables calculating unique values within a dataset
        dataset.load().waitForRecordsToLoad();
        dataset.load().waitForColumnStatsToLoad(); //loads all columns

        for (Column column : dataset.getColumns()) {
            System.out.println("Column Name: " + column.getName());
            for (Map.Entry<Object, LongAdder> entry : column.getUniqueValuesByCount()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("===============================");
        }

        dataset.close();
    }
}
