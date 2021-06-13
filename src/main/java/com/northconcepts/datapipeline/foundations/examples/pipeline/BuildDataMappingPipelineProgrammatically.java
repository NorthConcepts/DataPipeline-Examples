/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class BuildDataMappingPipelineProgrammatically {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef()
            .addEntity(new EntityDef().setName("Raw")
                .addField(new TextFieldDef().setName("event_type").setRequired(true).setMaximumLength(25))
                .addField(new TextFieldDef().setName("id").setRequired(true)))
            .addEntity(new EntityDef().setName("Processed")
                .addField(new TextFieldDef().setName("Event").setRequired(true).setMaximumLength(25))
                .addField(new NumericFieldDef().setName("Call ID").setType(FieldType.INT).setRequired(true)));

        DataMapping mapping = new DataMapping()
                .addFieldMapping(new FieldMapping("Event", "source.event_type"))
                .addFieldMapping(new FieldMapping("Call ID", "source.id"));

        LocalFileSource source = new LocalFileSource().setPath("input/file/sample/path.csv");
        LocalFileSink sink = new LocalFileSink().setPath("output/file/sample/path.csv");

        //Build DataMappingPipeline with source and target entities
        DataMappingPipeline pipeline = new DataMappingPipeline();
        pipeline.setInput(new CsvPipelineInput().setFileSource(source).setFieldNamesInFirstRow(true));
        pipeline.setSourceEntity(schema.getEntity("Raw"));
        pipeline.setDataMapping(mapping);
        pipeline.setTargetEntity(schema.getEntity("Processed"));
        pipeline.setOutput(new ExcelPipelineOutput().setFileSink(sink).setFieldNamesInFirstRow(true));
    }

}
