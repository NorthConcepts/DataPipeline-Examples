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
import com.northconcepts.datapipeline.foundations.pipeline.action.filter.FilterMatchExpression;
import com.northconcepts.datapipeline.foundations.pipeline.input.JsonPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.xml.XmlReader;

public class FilterJsonRecordsInAPipeline {

    public static void main(String... args) {
        Pipeline pipeline = new Pipeline();

        FileSource source = new LocalFileSource()
            .setName("input-json")
            .setPath("example/data/input/pipeline/MOCK_DATA.json")
            .detectFileType();
        
        JsonPipelineInput input = new JsonPipelineInput()
            .setFileSource(source)
            .addRecordBreak("//array/object")
            .addField("output_id", "//array/object/id")
            .addField("output_first_name", "//array/object/first_name")
            .addField("output_last_name", "//array/object/last_name")
            .addField("output_email", "//array/object/email")
            .addField("output_ip_address", "//array/object/ip_address")
            .setDuplicateFieldPolicy(XmlReader.DuplicateFieldPolicy.USE_LAST_VALUE);
        
        pipeline.setInput(input);

        FilterMatchExpression filter = new FilterMatchExpression();
        filter.setExpression("endsWith(output_email, 'org')");
        pipeline.addAction(filter);

        LocalFileSink sink = new LocalFileSink().setPath("data/output/test.xlsx");
        pipeline.setOutput(new ExcelPipelineOutput().setFileSink(sink).setFieldNamesInFirstRow(true));

        pipeline.run();
    }
}
