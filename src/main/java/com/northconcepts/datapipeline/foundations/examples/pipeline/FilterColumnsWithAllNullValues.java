package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.action.transform.SelectArrangeFieldsCommand;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;


public class FilterColumnsWithAllNullValues {

    public static void main(String[] args) {

        Pipeline pipeline = new Pipeline();

        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFile().setPath("data/input/Listing.csv"))
                .setFieldNamesInFirstRow(true);

        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
                .setFileSink(new LocalFile().setPath("data/output/NoNull.xlsx"))
                .setFieldNamesInFirstRow(true);

        pipeline.setInput(pipelineInput);
        pipeline.setOutput(pipelineOutput);

        Dataset dataset = pipeline.getDataset();
        dataset.load().waitForRecordsToLoad();

        SelectArrangeFieldsCommand action = new SelectArrangeFieldsCommand();

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