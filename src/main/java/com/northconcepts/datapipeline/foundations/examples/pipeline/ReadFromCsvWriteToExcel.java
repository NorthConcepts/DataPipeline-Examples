package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.action.convert.ConvertStringToNumberCommand;
import com.northconcepts.datapipeline.foundations.pipeline.action.transform.AddFieldsCommand;
import com.northconcepts.datapipeline.foundations.pipeline.action.transform.RenameFieldsCommand;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.job.Job;

import java.nio.channels.Pipe;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReadFromCsvWriteToExcel {

    public static void main(String[] args) throws Throwable{

        Pipeline pipeline = new Pipeline();

        LocalFile inputFile = new LocalFile()
                .setName("Input File")
                .setPath("PATH_TO_INPUT_FILE.csv")
                .detectFileType();

        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(inputFile)
                .setFieldNamesInFirstRow(true);

        LocalFile outputFile = new LocalFile()
                .setName("Output File")
                .setPath("PATH_TO_OUTPUT_FILE.xlsx")
                .detectFileType();

        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
                .setFileSink(outputFile)
                .setFieldNamesInFirstRow(true);

        pipeline.setInput(pipelineInput);
        pipeline.setOutput(pipelineOutput);

        pipeline.addAction(new RenameFieldsCommand().add("csv_original_column_name", "new_column_name"));
        pipeline.addAction(new ConvertStringToNumberCommand()
                .add("csv_listing_column", "csv_sell_column")
                .setType(ConvertStringToNumberCommand.FieldType.DOUBLE)
                .setPattern("0.00"));

        Map<String, AddFieldsCommand.TypeValue> map = new LinkedHashMap<>();
        map.put("new_column", new AddFieldsCommand.TypeValue(AddFieldsCommand.FieldType.EXPRESSION, "csv_listing_column - csv_sell_column"));

        AddFieldsCommand addFieldsCommand = new AddFieldsCommand().setMapping(map);

        pipeline.addAction(addFieldsCommand);

        Record pipelineRecord = pipeline.toRecord();

        Pipeline pipeline2 = new Pipeline();
        pipeline2.fromRecord(pipelineRecord);

        Job.run(pipeline2.createDataReader(true), pipeline2.createDataWriter());
    }
}
