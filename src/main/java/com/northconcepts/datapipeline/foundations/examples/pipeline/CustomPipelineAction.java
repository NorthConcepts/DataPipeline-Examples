package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.action.PipelineAction;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.transform.FieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class CustomPipelineAction {

    public static void main(String[] args) {
        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFile().setPath("example/data/input/countries_with_country-code.csv"))
                .setFieldNamesInFirstRow(true);
 
        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
                .setFileSink(new LocalFile().setPath("example/data/output/countries_with_country-code.xlsx"))
                .setFieldNamesInFirstRow(true);
 
        Pipeline pipeline = new Pipeline()
            .setInput(pipelineInput)
            .setOutput(pipelineOutput)
            .addAction(new UpcaseFieldsAction().addField("Country"));
        
        pipeline.run();
    }
    
}

class UpcaseFieldsAction extends PipelineAction {

    private final List<String> fieldNameList;

    protected UpcaseFieldsAction() {
        super("transform-rename-fields", "Upper Case Fields");
        fieldNameList = new ArrayList<>();
    }

    @Override
    public DataReader apply(DataReader reader) throws Throwable {
        TransformingReader transformingReader = new TransformingReader(reader)
                .add(new UpcaseField(fieldNameList));
        return transformingReader;
    }

    public UpcaseFieldsAction addField(String fieldName) {
        fieldNameList.add(fieldName);
        return this;
    }

    @Override
    public void fromRecord(Record source) {
    }

    @Override
    public Record toRecord() {
        return null;
    }

}

class UpcaseField extends FieldTransformer {

    private final List<String> fieldNameList;

    public UpcaseField(List<String> fieldNameList) {
        super(fieldNameList.toArray(new String[fieldNameList.size()]));
        this.fieldNameList = fieldNameList;
    }

    @Override
    protected void transformField(Field field) throws Throwable {
        if (fieldNameList.contains(field.getName()) && FieldType.STRING.equals(field.getType()) && field.isNotNull()) {
            field.setValue(field.getValueAsString().toUpperCase());
        }
    }

}
