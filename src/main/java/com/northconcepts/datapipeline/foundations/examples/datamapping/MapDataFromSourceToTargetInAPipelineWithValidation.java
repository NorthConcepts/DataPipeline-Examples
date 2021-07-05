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
import com.northconcepts.datapipeline.filter.FieldCount;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.DataWriterPipelineOutput;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;

public class MapDataFromSourceToTargetInAPipelineWithValidation {

    public static void main(String[] args) {
        SchemaDef schema = new SchemaDef()
                .addEntity(new EntityDef().setName("Product")
                        .addField(new TextFieldDef("Title", FieldType.STRING).setRequired(true).setMinimumLength(5).setMaximumLength(100))
//                        .addField(new NumericFieldDef("Price", FieldType.DOUBLE).setRequired(true).setMinimum(10.0))
                        .addField(new NumericFieldDef("Price", FieldType.BIG_DECIMAL).setRequired(true).setMinimum(10.0))
                        .addValidation(new FieldCount(3)));
        
        EntityDef productEntity = schema.getEntity("Product");
        
        DataMapping mapping = new DataMapping()
                .setValue("Markup", BigDecimal.valueOf(10.00))  // add a constant to be used in the mapping
                .addFieldMapping(new FieldMapping("Title", "coalesce(source.Title, source.Handle)"))
                .addFieldMapping(new FieldMapping("Cost", "${source.Variant Price}"))  // use ${} since field has space in name
                .addFieldMapping(new FieldMapping("Price", "toBigDecimal(target.Cost) + Markup"));

        LocalFileSource fileSource = new LocalFileSource().setPath("data/input/jewelry.csv");

        DataMappingPipeline pipeline = new DataMappingPipeline()
            .setDataMapping(mapping)
            .setTargetEntity(productEntity)
            .setInput(new CsvPipelineInput().setFileSource(fileSource).setAllowMultiLineText(true).setFieldNamesInFirstRow(true))
            .setOutput(new DataWriterPipelineOutput(() -> StreamWriter.newSystemOutWriter()));

        pipeline.run();
    }

}
