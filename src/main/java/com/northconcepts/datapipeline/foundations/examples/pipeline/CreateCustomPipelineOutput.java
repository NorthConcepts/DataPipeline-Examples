/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
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

        Record record = pipeline.toRecord();
        System.out.println(record);

        System.out.println("---------------------------------------------------------------------------------------------------------");

        pipeline = new Pipeline().fromRecord(record);
        pipeline.run();

        System.out.println("---------------------------------------------------------------------------------------------------------");

        System.out.println("Pipeline as JSON:");
        System.out.println(Util.formatJson(pipeline.toJson()));

        System.out.println("---------------------------------------------------------------------------------------------------------");

        pipeline = new Pipeline().fromXml(pipeline.toXml());
        pipeline.run();

        System.out.println("---------------------------------------------------------------------------------------------------------");

        System.out.println("Pipeline as XML:");
        System.out.println(pipeline.toXml());
    }

    public static class CustomPipelineOutput extends PipelineOutput {

        @Override
        public DataWriter createDataWriter() {
            return new ConsoleWriter();
        }

        @Override
        public String getName() {
            return "CustomPipelineOutput";
        }

        @Override
        public Record toRecord() {
            return super.toRecord();
        }

        @Override
        public PipelineOutput fromRecord(Record source) {
            super.fromRecord(source);
            return this;
        }

        @Override
        public Element toXmlElement(Document document) {
            return super.toXmlElement(document);
        }

        @Override
        public CustomPipelineOutput fromXmlElement(Element element) {
            return this;
        }

    }

    public static class ConsoleWriter extends DataWriter {

        @Override
        protected void writeImpl(Record record) throws Throwable {
            System.out.println(record);
        }

    }
}
