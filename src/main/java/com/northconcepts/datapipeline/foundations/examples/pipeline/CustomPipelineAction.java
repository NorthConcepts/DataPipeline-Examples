/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.ArrayValue;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.action.PipelineAction;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.foundations.sourcecode.CodeWriter;
import com.northconcepts.datapipeline.foundations.sourcecode.JavaCodeBuilder;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
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
        
        System.out.println("Generated Code:" + System.lineSeparator());
        System.out.println(pipeline.getJavaCode().getSource());
    }
    
    static class UpcaseFieldsAction extends PipelineAction {

        private final List<String> fieldNameList;

        protected UpcaseFieldsAction() {
            super("transform-upcase-fields", "Upper Case Fields");
            fieldNameList = new ArrayList<>();
        }

        @Override
        public DataReader apply(DataReader reader) throws Throwable {
            TransformingReader transformingReader = new TransformingReader(reader)
                    .add(new BasicFieldTransformer(fieldNameList.toArray(new String[fieldNameList.size()])).upperCase());
            return transformingReader;
        }

        public UpcaseFieldsAction addField(String fieldName) {
            fieldNameList.add(fieldName);
            return this;
        }

        @Override
        public void fromRecord(Record source) {
            if(source.containsField("fieldNameList")) {
                ArrayValue array = source.getField("fieldNameList").getValueAsArray();
                for (int i = 0; i < array.size(); i++) {
                    addField(array.getValueAsRecord(i).getValueAsString());
                }
            }
        }

        @Override
        public Record toRecord() {
            Record record = new Record();

            if (Util.isNotEmpty(fieldNameList)) {
                ArrayValue array = new ArrayValue();
                for (String fieldName : fieldNameList) {
                    array.addValue(fieldName);
                }
                record.setField("fieldNameList", array);
            }
            return record;
        }
        
        @Override
        public void generateJavaCode(JavaCodeBuilder code) {
            super.generateJavaCode(code);

            CodeWriter writer = code.getSourceWriter();

            writer.println("reader = new TransformingReader(reader)");
            writer.indent();
            
            if (Util.isNotEmpty(fieldNameList)) {
                for (String fieldName : fieldNameList) {
                    writer.println(".add(new UpcaseFieldsAction().addField(\"%s\"))", escapeJavaString(fieldName));
                }
            }
            writer.println(";");
            writer.outdent();
        }

    }
    
}

