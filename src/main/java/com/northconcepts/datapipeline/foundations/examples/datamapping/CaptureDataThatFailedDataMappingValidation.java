/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.math.BigDecimal;

import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;

public class CaptureDataThatFailedDataMappingValidation {

    public static void main(String... args) {
        EntityDef targetEntity = new EntityDef()
            .addField(new NumericFieldDef("Price", FieldType.DOUBLE).setRequired(true).setMinimum(50));

        DataMapping mapping = new DataMapping()
            .setValue("Markup", BigDecimal.valueOf(10.00)) // add a constant to be used in the mapping
            .addFieldMapping(new FieldMapping("Title", "coalesce(source.Title, source.Handle)"))
            .addFieldMapping(new FieldMapping("Cost", "${source.Variant Price}")) // use ${} since field has space in name
            .addFieldMapping(new FieldMapping("Price", "toBigDecimal(target.Cost) + Markup"));

        LocalFileSource source = new LocalFileSource().setPath("data/input/jewelry.csv");
        LocalFileSink sink = new LocalFileSink().setPath("data/output/test.xlsx");

        MemoryWriter discardWriter = new MemoryWriter();

        // Build DataMappingPipeline with source and target entities
        DataMappingPipeline pipeline = new DataMappingPipeline();
        pipeline.setDiscardReasonFieldName("errorField");
        pipeline.setDiscardWriter(discardWriter);
        pipeline.setInput(new CsvPipelineInput().setFileSource(source).setFieldNamesInFirstRow(true).setAllowMultiLineText(true));
        pipeline.setSourceEntity(targetEntity);
        pipeline.setDataMapping(mapping);
        pipeline.setOutput(new ExcelPipelineOutput().setFileSink(sink).setFieldNamesInFirstRow(true));

        // Run the pipeline
        pipeline.run();

        System.out.println("Discarded Records");
        Job.run(new MemoryReader(discardWriter.getRecordList()), StreamWriter.newSystemOutWriter()); // print discarded records
    }
}
