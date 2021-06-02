/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.file.LocalFile;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;

public class MapRecordsInPipelineUsingSchema {

    public static void main(String[] args) {
        
        CsvPipelineInput pipelineInput = new CsvPipelineInput()
                .setFieldNamesInFirstRow(true)
                .setAllowMultiLineText(true);

        pipelineInput.setFileSource(new LocalFile().setPath("data/input/jewelry.csv"));  // can be set late with actual file

        EntityDef entityDef = new EntityDef().setName("Jewelry")
                .addField(new NumericFieldDef("Variant Price", FieldType.DOUBLE))
                .addField(new BooleanFieldDef("Variant Taxable", FieldType.BOOLEAN))
                .addField(new BooleanFieldDef("Published", FieldType.BOOLEAN))
                .addField(new BooleanFieldDef("Variant Requires Shipping", FieldType.BOOLEAN))
                .addField(new BooleanFieldDef("Variant Taxable", FieldType.BOOLEAN))
                .addField(new BooleanFieldDef("Gift Card", FieldType.BOOLEAN))
                .addField(new NumericFieldDef("Variant Grams", FieldType.INT))
                .addField(new NumericFieldDef("Variant Inventory Qty", FieldType.INT))
                .addField(new NumericFieldDef("Image Position", FieldType.INT))
                .addField(new TextFieldDef("Title", FieldType.STRING).setMaximumLength(256))
                .addField(new TextFieldDef("Option1 Value", FieldType.STRING))
                .setAllowExtraFieldsInMapping(true);
        
        Pipeline pipeline = new Pipeline(); 
        pipeline.setInput(pipelineInput);
        pipeline.setSourceEntity(entityDef);
        

        DataReader reader = pipeline.createDataReader();
        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }

}
