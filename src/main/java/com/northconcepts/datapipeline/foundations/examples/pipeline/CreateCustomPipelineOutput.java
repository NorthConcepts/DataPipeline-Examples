/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineOutput;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.sourcecode.CodeWriter;
import com.northconcepts.datapipeline.foundations.sourcecode.JavaCodeBuilder;
import com.northconcepts.datapipeline.internal.lang.Util;

public class CreateCustomPipelineOutput {

    public static void main(String[] args) {
        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFileSource(new LocalFileSource().setPath("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        CustomPipelineOutput pipelineOutput = new CustomPipelineOutput();

        Pipeline pipeline = new Pipeline()
                .setInput(pipelineInput)
                .setOutput(pipelineOutput);

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

    public static class CustomPipelineOutput extends PipelineOutput {

        @Override
        public DataWriter createDataWriter() {
            return new ConsoleWriter();
        }

        @Override
        public void generateJavaCode(JavaCodeBuilder code) {
            
            code.addImport("java.io.File");
            code.addImport("com.northconcepts.datapipeline.core.DataWriter");
            code.addImport("com.northconcepts.datapipeline.foundations.examples.pipeline.CreateCustomPipelineOutput.CustomPipelineOutput");
            code.addImport("com.northconcepts.datapipeline.foundations.examples.pipeline.CreateCustomPipelineOutput.ConsoleWriter");

            CodeWriter writer = code.getSourceWriter();

            writer.println();
            writer.println("DataWriter writer = new ConsoleWriter();");
        }

        @Override
        public String getName() {
            return "CustomPipelineOutput";
        }

        @Override
        public Record toRecord() {
            return new Record();
        }

        @Override
        public PipelineOutput fromRecord(Record source) {
            return this;
        }
        
        @Override
        public Element toXmlElement(Document document) {
            return super.toXmlElement(document);
        }

    }

    public static class ConsoleWriter extends DataWriter {

        @Override
        protected void writeImpl(Record record) throws Throwable {
            System.out.println(record);
        }

    }
}
