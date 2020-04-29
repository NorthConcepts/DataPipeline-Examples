package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.ExcelPipelineInput;

public class FilterColumnsWithAllNullValues {

    public static void main(String[] args) {

        Pipeline pipeline = new Pipeline();

        LocalFile inputFile = new LocalFile()
                .setName("Input File")
                .setPath("PATH_TO_INPUT_FILE.csv")
                .detectFileType();

        ExcelPipelineInput pipelineInput = new ExcelPipelineInput()
                .setFileSource(inputFile)
                .setFieldNamesInFirstRow(true);

        pipeline.setInput(pipelineInput);

        Dataset dataset = pipeline.getDataset();
        dataset.load().waitForRecordsToLoad();

        for(Column column : dataset.getColumns()) {
            if(column.getValueCount() == column.getNullCount()) {
                continue;
            }
            System.out.println(column.getName());
        }

    }
}
