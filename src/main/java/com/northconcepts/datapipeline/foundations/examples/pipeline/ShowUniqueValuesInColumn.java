/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
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

        Dataset dataset = new MemoryDataset(pipeline);
        dataset.load().waitForRecordsToLoad();

        for(Column column : dataset.getColumns()) {
            System.out.println("Column Name: " + column.getName());
            for(Map.Entry<Object, LongAdder> entry : column.getUniqueValuesByCount()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("===============================");
        }

        dataset.close();
    }
}
