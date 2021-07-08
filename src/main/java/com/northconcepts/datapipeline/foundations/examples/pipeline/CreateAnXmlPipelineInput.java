/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.foundations.file.FileSource;
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.input.XmlPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.xml.XmlReader;

public class CreateAnXmlPipelineInput {

    public static void main(String... args) {
        Pipeline pipeline = new Pipeline();

        FileSource source = new LocalFileSource()
            .setName("input-xml")
            .setPath("example/data/input/pipeline/xml-input.xml")
            .detectFileType();

        XmlPipelineInput input = new XmlPipelineInput()
            .setFileSource(source)
            .addField("teacherId", "/teachers/teacher/@memberid")
            .addField("name", "/teachers/teacher/name", "/teachers/teacher")
            .addField("gender", "/teachers/teacher/gender", true)
            .addField("subjects", "/teachers/teacher/subjects/subject/@name")
            .addRecordBreak("/teachers/teacher/subjects")
            .setDuplicateFieldPolicy(XmlReader.DuplicateFieldPolicy.COPY_FIELD);

        pipeline.setInput(input);

        LocalFileSink sink = new LocalFileSink().setPath("data/output/test.xlsx");
        pipeline.setOutput(new ExcelPipelineOutput().setFileSink(sink).setFieldNamesInFirstRow(true));

        pipeline.run();
    }
}
