/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.action.transform.SelectArrangeFieldsAction;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;


public class FilterColumnsWithAllNullValues {

    public static void main(String[] args) {

        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFileSource().setPath("data/input/Listing.csv"))
                .setFieldNamesInFirstRow(true);

        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
                .setFileSink(new LocalFileSink().setPath("data/output/NoNull.xlsx"))
                .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline()
            .setInput(pipelineInput)
            .setOutput(pipelineOutput);

        Dataset dataset = new MemoryDataset(pipeline);
        dataset.load().waitForRecordsToLoad();

        SelectArrangeFieldsAction action = new SelectArrangeFieldsAction();

        for(Column column : dataset.getColumns()) {
            if(column.getValueCount() != column.getNullCount()) {
                action.add(column.getName());
            }
        }

        pipeline.addAction(action);

        pipeline.run();

        dataset.close();
    }
}
