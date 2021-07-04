/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import static com.northconcepts.datapipeline.core.XmlSerializable.getAttribute;
import static com.northconcepts.datapipeline.core.XmlSerializable.setAttribute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.PipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.internal.lang.Util;

public class CreateCustomPipelineInput {

    public static void main(String[] args) {
        CustomPipelineInput pipelineInput = new CustomPipelineInput();

        ExcelPipelineOutput pipelineOutput = new ExcelPipelineOutput()
                .setFileSink(new LocalFileSink().setPath("example/data/output/custom_package_pipeline_input.xlsx"))
                .setFieldNamesInFirstRow(true);

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

    public static class CustomPipelineInput extends PipelineInput {

        private static int maxTrucks = 10;
        private static int maxPackages = 20;
        private static int recordDelayMS = 250;

        public static int getMaxTrucks() {
            return maxTrucks;
        }

        public static void setMaxTrucks(int maxTrucks) {
            CustomPipelineInput.maxTrucks = maxTrucks;
        }

        public static int getMaxPackages() {
            return maxPackages;
        }

        public static void setMaxPackages(int maxPackages) {
            CustomPipelineInput.maxPackages = maxPackages;
        }

        public static int getRecordDelayMS() {
            return recordDelayMS;
        }

        public static void setRecordDelayMS(int recordDelayMS) {
            CustomPipelineInput.recordDelayMS = recordDelayMS;
        }

        @Override
        public String getName() {
            return "CustomPipelineInput";
        }

        @Override
        public Record toRecord() {
            return super.toRecord()
                    .setField("maxTrucks", maxTrucks)
                    .setField("maxPackages", maxPackages)
                    .setField("recordDelayMS", recordDelayMS);
        }

        @Override
        public CustomPipelineInput fromRecord(Record source) {
            super.fromRecord(source);
            setMaxTrucks(source.getFieldValueAsInteger("maxTrucks", maxTrucks));
            setMaxPackages(source.getFieldValueAsInteger("maxPackages", maxPackages));
            setRecordDelayMS(source.getFieldValueAsInteger("recordDelayMS", recordDelayMS));

            return this;
        }

        @Override
        public Element toXmlElement(Document document) {
            Element element = super.toXmlElement(document);
            setAttribute(element, "maxTrucks", maxTrucks);
            setAttribute(element, "maxPackages", maxPackages);
            setAttribute(element, "recordDelayMS", recordDelayMS);
            return element;
        }

        @Override
        public CustomPipelineInput fromXmlElement(Element element) {
            setMaxTrucks(getAttribute(element, "maxTrucks", maxTrucks));
            setMaxPackages(getAttribute(element, "maxPackages", maxPackages));
            setRecordDelayMS(getAttribute(element, "recordDelayMS", recordDelayMS));
            return this;
        }

        @Override
        public DataReader createDataReader() {
            return new FakePackageReader(maxTrucks, maxPackages, recordDelayMS);
        }

    }

    public static class FakePackageReader extends DataReader {

        private final int maxTrucks;
        private final long maxPackages;
        private long nextPackageId;
        private final long recordDelay;

        public FakePackageReader(int maxTrucks, long maxPackages, long recordDelay) {
            this.maxTrucks = maxTrucks;
            this.maxPackages = maxPackages;
            this.recordDelay = recordDelay;
        }

        @Override
        protected Record readImpl() throws Throwable {
            if (nextPackageId >= maxPackages) {
                return null;
            }

            if (recordDelay > 0) {
                Thread.sleep(recordDelay);
            }

            Record record = new Record();
            record.setField("package_id", nextPackageId++);
            record.setField("truck_id", "truck" + nextPackageId % maxTrucks);
            record.setField("amount", nextPackageId + 0.01);
            return record;
        }
    }

}
