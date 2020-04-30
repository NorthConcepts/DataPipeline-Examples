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

import java.util.LinkedHashMap;
import java.util.Map;

public class SaveAndRestorePipelineFromJson {

    public static void main(String[] args) throws Throwable{

        Pipeline pipeline = new Pipeline();

        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFile().setPath("data/input/Listing.csv"))
                .setFieldNamesInFirstRow(true);

        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
                .setFileSink(new LocalFile().setPath("data/output/output.xlsx"))
                .setFieldNamesInFirstRow(true);

        pipeline.setInput(pipelineInput);
        pipeline.setOutput(pipelineOutput);

        pipeline.addAction(new RenameFieldsCommand().add("Taxes", "Taxes_Renamed"));
        pipeline.addAction(new ConvertStringToNumberCommand()
                .add("Sell", "List")
                .setType(ConvertStringToNumberCommand.FieldType.DOUBLE)
                .setPattern("0.00"));

        Map<String, AddFieldsCommand.TypeValue> map = new LinkedHashMap<>();
        map.put("new_column", new AddFieldsCommand.TypeValue(AddFieldsCommand.FieldType.EXPRESSION, "List - Sell"));

        AddFieldsCommand addFieldsCommand = new AddFieldsCommand().setMapping(map);

        pipeline.addAction(addFieldsCommand);

        Record pipelineRecord = pipeline.toRecord();

        Pipeline pipeline2 = new Pipeline();
        pipeline2.fromRecord(pipelineRecord);

        pipeline2.run();
    }
}
