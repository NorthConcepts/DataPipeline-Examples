/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
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
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.action.PipelineAction;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.foundations.sourcecode.CodeWriter;
import com.northconcepts.datapipeline.foundations.sourcecode.JavaCodeBuilder;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class CreateCustomPipelineAction {

    public static void main(String[] args) {
        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFileSource().setPath("example/data/input/countries_with_country-code.csv"))
                .setFieldNamesInFirstRow(true);

        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
                .setFileSink(new LocalFileSink().setPath("example/data/output/countries_with_country-code.xlsx"))
                .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline()
                .setInput(pipelineInput)
                .setOutput(pipelineOutput)
                .addAction(new UpcaseFieldsAction().addField("Country"));

        pipeline.run();

        System.out.println("---------------------------------------------------------------------------------------------------------");

        System.out.println("Generated Code:");
        System.out.println(pipeline.getJavaCode().getSource());

        System.out.println("---------------------------------------------------------------------------------------------------------");

        Record record = pipeline.toRecord();
        System.out.println(record);

        System.out.println("---------------------------------------------------------------------------------------------------------");

        Pipeline pipeline2 = new Pipeline().fromRecord(record);
        pipeline2.run();

        System.out.println("---------------------------------------------------------------------------------------------------------");

        System.out.println("Pipeline as JSON:");
        System.out.println(Util.formatJson(pipeline.toJson()));

    }

    public static class UpcaseFieldsAction extends PipelineAction {

        private final List<String> fieldNameList = new ArrayList<>();

        public UpcaseFieldsAction() {
            super("transform-upcase-fields", "UpperCase Fields");
        }

        @Override
        public DataReader apply(DataReader reader) throws Throwable {
            TransformingReader transformingReader = new TransformingReader(reader)
                    .add(new BasicFieldTransformer(fieldNameList).upperCase());
            return transformingReader;
        }

        public UpcaseFieldsAction addField(String fieldName) {
            fieldNameList.add(fieldName);
            return this;
        }

        @Override
        public Record toRecord() {
            Record record = super.toRecord();

            ArrayValue array = new ArrayValue();
            for (String fieldName : fieldNameList) {
                array.addValue(fieldName);
            }
            record.setField("fieldNameList", array);
            return record;
        }

        @Override
        public UpcaseFieldsAction fromRecord(Record source) {
            super.fromRecord(source);

            if (source.containsField("fieldNameList")) {
                ArrayValue array = source.getField("fieldNameList").getValueAsArray();
                for (int i = 0; i < array.size(); i++) {
                    addField(array.getValueAsString(i));
                }
            }
            return this;
        }

        @Override
        public void generateJavaCode(JavaCodeBuilder code) {
            super.generateJavaCode(code);

            code.addImport("com.northconcepts.datapipeline.transform.TransformingReader");
            code.addImport("com.northconcepts.datapipeline.transform.BasicFieldTransformer");

            CodeWriter writer = code.getSourceWriter();

            writer.println("reader = new TransformingReader(reader)");
            writer.indent();

            writer.println(".add(new BasicFieldTransformer(\"%s\").upperCase());", escapeJavaString(String.join("\",\"", fieldNameList)));

            writer.println(";");
            writer.outdent();
        }

    }

}
