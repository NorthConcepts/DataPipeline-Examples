/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.DataWriterPipelineOutput;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class MapDataFromSourceToTargetUsingDataMappingPipeline {

    public static void main(String[] args) {
        LocalFile localFile = new LocalFile().setPath("example/data/input/call-center-inbound-call.csv");

        SchemaDef schema = new SchemaDef()
            .addEntity(new EntityDef().setName("Raw")
                .addField(new TextFieldDef().setName("event_type").setRequired(true).setMaximumLength(25))
                .addField(new TextFieldDef().setName("id").setRequired(true))
                .addField(new TextFieldDef().setName("agent_id").setRequired(true))
                .addField(new TextFieldDef().setName("phone_number").setRequired(true).setMinimumLength(9))
                .addField(new TextFieldDef().setName("start_time").setRequired(true))
                .addField(new TextFieldDef().setName("end_time"))
                .addField(new TextFieldDef().setName("disposition")))

            .addEntity(new EntityDef().setName("Processed")
                .addField(new TextFieldDef().setName("Event").setRequired(true).setMaximumLength(25))
                .addField(new NumericFieldDef().setName("Call ID").setType(FieldType.INT).setRequired(true))
                .addField(new NumericFieldDef().setName("Agent ID").setType(FieldType.INT).setRequired(true))
                .addField(new TextFieldDef().setName("Caller Number").setRequired(true).setMinimumLength(9))
                .addField(new TemporalFieldDef().setName("Call Start Time").setRequired(true).setType(FieldType.TIME))
                .addField(new TemporalFieldDef().setName("Call End Time").setRequired(true).setType(FieldType.TIME))
                .addField(new TextFieldDef().setName("Disposition").setRequired(true).setDefaultValueExpression("'UNKNOWN'")));

        DataMapping mapping = new DataMapping()
                .addFieldMapping(new FieldMapping("Event", "source.event_type"))
                .addFieldMapping(new FieldMapping("Call ID", "source.id"))
                .addFieldMapping(new FieldMapping("Agent ID", "toInt(source.agent_id)"))
                .addFieldMapping(new FieldMapping("Caller Number", "source.phone_number"))
                .addFieldMapping(new FieldMapping("Call Start Time", "toTime(parseDate(source.start_time,'yyyy-MM-dd HH:mm'))"))
                .addFieldMapping(new FieldMapping("Call End Time", "toTime(parseDate(source.end_time,'yyyy-MM-dd HH:mm'))").setDefaultValueExpression("${target.Call Start Time}"))
                .addFieldMapping(new FieldMapping("Disposition", "source.disposition"));


        DataMappingPipeline pipeline = new DataMappingPipeline();
        pipeline.setInput(new CsvPipelineInput().setFileSource(localFile).setFieldNamesInFirstRow(true));
        pipeline.setSourceEntity(schema.getEntity("Raw"));
        pipeline.setDataMapping(mapping);
        pipeline.setTargetEntity(schema.getEntity("Processed"));
        pipeline.setOutput(new DataWriterPipelineOutput(() -> StreamWriter.newSystemOutWriter()));

        pipeline.run();
    }

}
