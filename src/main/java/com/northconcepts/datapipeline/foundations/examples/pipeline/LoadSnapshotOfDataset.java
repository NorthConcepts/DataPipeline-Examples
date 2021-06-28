/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.util.concurrent.TimeUnit;

import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;

public class LoadSnapshotOfDataset {

    public static void main(String[] args) {
        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFileSource().setPath("example/data/input/trades.csv"))
                .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline();

        pipeline.setInput(pipelineInput);
        
        Dataset dataset = new MemoryDataset(pipeline);
        dataset.load().waitForRecordsToLoad(10000, TimeUnit.SECONDS.toMillis(4));

        System.out.println("Total Records:- " + dataset.getRecordCount());
    }
}
