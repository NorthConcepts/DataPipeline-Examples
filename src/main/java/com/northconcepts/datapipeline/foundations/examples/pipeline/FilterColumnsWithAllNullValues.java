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

import java.util.concurrent.TimeUnit;


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
        dataset.load().waitForRecordsToLoad(100, TimeUnit.SECONDS.toMillis(4));

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
