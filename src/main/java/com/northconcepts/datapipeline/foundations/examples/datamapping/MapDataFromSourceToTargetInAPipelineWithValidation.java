/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.File;
import java.math.BigDecimal;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filter.FieldCount;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;

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
                .setTargetValidationEntity(productEntity)   // set optional entity definition to validate against
                .setValue("Markup", BigDecimal.valueOf(10.00))  // add a constant to be used in the mapping
                .addFieldMapping(new FieldMapping("Title", "coalesce(source.Title, source.Handle)"))
                .addFieldMapping(new FieldMapping("Cost", "${source.Variant Price}"))  // use ${} since field has space in name
                .addFieldMapping(new FieldMapping("Price", "toBigDecimal(target.Cost) + Markup"));
        
        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);
        
        reader = new DataMappingReader(reader, mapping);
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }

}
