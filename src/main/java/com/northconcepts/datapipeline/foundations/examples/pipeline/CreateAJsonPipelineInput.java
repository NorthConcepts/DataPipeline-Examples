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
import com.northconcepts.datapipeline.foundations.pipeline.input.JsonPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.xml.XmlReader;

public class CreateAJsonPipelineInput {

    public static void main(String... args) {
        Pipeline pipeline = new Pipeline();

        FileSource source = new LocalFileSource()
            .setName("input-json")
            .setPath("example/data/input/pipeline/json-input.json")
            .detectFileType();
        
        JsonPipelineInput input = new JsonPipelineInput()
            .setFileSource(source)
            .addRecordBreak("//array/object")
            .addField("UID", "/object/UID", true)
            .addField("type", "//favorites/*/node()")
            .addField("favorite-name", "//favorites//name")
            .addField("favorite-id", "//favorites//id", "//favorites/books")
            .addField("favorite-category", "//favorites//category")
            .setDuplicateFieldPolicy(XmlReader.DuplicateFieldPolicy.USE_LAST_VALUE);
        
        pipeline.setInput(input);

        LocalFileSink sink = new LocalFileSink().setPath("data/output/test.xlsx");
        pipeline.setOutput(new ExcelPipelineOutput().setFileSink(sink).setFieldNamesInFirstRow(true));

        pipeline.run();
    }
}
