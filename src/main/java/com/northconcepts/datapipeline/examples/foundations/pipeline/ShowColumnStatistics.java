/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.foundations.pipeline;

import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.input.ExcelPipelineInput;

public class ShowColumnStatistics {

    public static void main(String[] args) {

        Pipeline pipeline = new Pipeline();

        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFile().setPath("data/input/Listing.csv"))
                .setFieldNamesInFirstRow(true);

        pipeline.setInput(pipelineInput);

        Dataset dataset = pipeline.getDataset();
        dataset.load().waitForRecordsToLoad();

        for(Column column : dataset.getColumns()) {
            System.out.println("Name:" + column.getName());
            System.out.println("Value Count: " + column.getValueCount());
            System.out.println("Null Count: " + column.getNullCount());
            System.out.println("Blank Count: " + column.getBlankCount());
            System.out.println("Unique Value Count " + column.getUniqueValueCount());
            System.out.println("Is Numerical Column: " + column.getNumerical());
            System.out.println("Is Temporal Column: " + column.getTemporal());
            System.out.println("Is Boolean Column: " + column.getBoolean());
            System.out.println("Minimum Length: " + column.getMinimumLength());
            System.out.println("Maximum Length: " + column.getMaximumLength());
            System.out.println("Sample Value: " + column.getSampleValue());
            System.out.println("===============================");

        }

    }
}
