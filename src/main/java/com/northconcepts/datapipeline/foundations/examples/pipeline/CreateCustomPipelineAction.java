/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import static com.northconcepts.datapipeline.core.XmlSerializable.getAttribute;
import static com.northconcepts.datapipeline.core.XmlSerializable.getChildElement;
import static com.northconcepts.datapipeline.core.XmlSerializable.getChildElements;
import static com.northconcepts.datapipeline.core.XmlSerializable.setAttribute;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.northconcepts.datapipeline.core.ArrayValue;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.action.PipelineAction;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
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
        public Element toXmlElement(Document document) {
            Element element = super.toXmlElement(document);

            Element fieldsNameListElement = document.createElement("fieldNameList");
            for(String fieldName : fieldNameList) {
                Element fieldNameElement = document.createElement("field");
                setAttribute(fieldNameElement, "name", fieldName);
                fieldsNameListElement.appendChild(fieldNameElement);
            }
            element.appendChild(fieldsNameListElement);

            return element;
        }

        @Override
        public UpcaseFieldsAction fromXmlElement(Element element) {
            super.fromXmlElement(element);

            Element fieldsNameListElement = getChildElement(element, "fieldNameList");
            if(fieldsNameListElement != null) {
                List<Element> fieldsElementList = getChildElements(fieldsNameListElement, "field");
                for(Element field : fieldsElementList) {
                    fieldNameList.add(getAttribute(field, "name"));
                }
            }

            return this;
        }

    }

}
