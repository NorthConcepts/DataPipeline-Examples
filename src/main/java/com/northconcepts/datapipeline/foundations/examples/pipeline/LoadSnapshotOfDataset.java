package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.util.concurrent.TimeUnit;

import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;

public class LoadSnapshotOfDataset {

    public static void main(String[] args) {
        Pipeline pipeline = new Pipeline();

        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFile().setPath("example/data/input/trades.csv"))
                .setFieldNamesInFirstRow(true);

        pipeline.setInput(pipelineInput);
        
        Dataset dataset = pipeline.getDataset();
        dataset.load().waitForRecordsToLoad(10000, TimeUnit.SECONDS.toMillis(4));

        System.out.println("Total Records:- " + dataset.getRecordCount());
    }
}
