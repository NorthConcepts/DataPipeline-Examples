/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.File;
import java.io.FileInputStream;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.file.LocalFileSink;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;
import com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput;
import com.northconcepts.datapipeline.foundations.pipeline.output.ExcelPipelineOutput;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.transform.lookup.BasicLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;

public class MapDataWithSchemaBasedValidation {

    public static void main(String[] args) throws Throwable {
        // Load source & target schema
        SchemaDef schema = new SchemaDef()
            .fromXml(new FileInputStream(new File("example/data/input/datamapping/account-schema-definition.xml")));
        EntityDef sourceAccountEntity = schema.getEntity("SourceAccountEntity");
        EntityDef targetAccountEntity = schema.getEntity("TargetAccountEntity");

        ((NumericFieldDef) sourceAccountEntity.getField("Balance"))
            .setMinimum(10000D)
            .setMaximum(100000000D);
        ((NumericFieldDef) sourceAccountEntity.getField("CreditLimit"))
            .setMinimum(9999D)
            .setMaximum(9999999D);

        // Create hard-coded lookup
        Lookup statusLookup = new BasicLookup(new FieldList("status"))
            .add("A", "Updated")
            .add("B", "Late")
            .add("C", "Overdue")
            .add("D", "Default");

        DataMapping mapping = new DataMapping()
            .fromXml(new FileInputStream("example/data/input/datamapping/credit-balance-mapping.xml"))
            .setValue("statusLookup", statusLookup);

        LocalFileSource source = new LocalFileSource().setPath("example/data/input/credit-balance-02-100000.csv");
        LocalFileSink sink = new LocalFileSink().setPath("data/output/DeclarativelyMapDataWithSourceAndTargetSchema.xlsx");

        DataMappingPipeline pipeline = new DataMappingPipeline();
        pipeline.setInput(new CsvPipelineInput().setFileSource(source).setFieldNamesInFirstRow(true).setAllowMultiLineText(true));
        pipeline.setSourceEntity(sourceAccountEntity);
        pipeline.setDataMapping(mapping);
        pipeline.setTargetEntity(targetAccountEntity);
        pipeline.setOutput(new ExcelPipelineOutput().setFileSink(sink).setFieldNamesInFirstRow(true));

        pipeline.run();
    }

}
