/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.File;
import java.io.FileInputStream;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.BasicLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;

public class DeclarativelyMapData {

    public static void main(String... args) throws Throwable {
        // Load source & target schema
        SchemaDef schema = new SchemaDef()
                .fromXml(new FileInputStream(new File("example/data/input/datamapping/account-schema-definition.xml")));
        EntityDef sourceAccountEntity = schema.getEntity("SourceAccountEntity");
        EntityDef targetAccountEntity = schema.getEntity("TargetAccountEntity");

        // Create hard-coded lookup
        Lookup statusLookup = new BasicLookup(new FieldList("status"))
            .add("A", "Updated")
            .add("B", "Late")
            .add("C", "Overdue")
            .add("D", "Default");

        // Define job
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02-100000.csv"))  // 1mm -> credit-balance-02-1000000.csv
                .setFieldNamesInFirstRow(true);
        
        reader = new TransformingReader(reader)
                .add(new SchemaTransformer(sourceAccountEntity));
        
//        reader = new AsyncReader(reader);  // Threading
        
        DataMapping mapping = new DataMapping()
                .fromXml(new FileInputStream("example/data/input/datamapping/credit-balance-mapping.xml"));
        mapping.setValue("statusLookup", statusLookup);
        reader = new DataMappingReader(reader, mapping);

//        reader = new AsyncReader(reader);  // Threading
        
        reader = new TransformingReader(reader)
                .add(new SchemaTransformer(targetAccountEntity));
        
//        reader = new AsyncReader(reader);  // Threading

        DataWriter writer = new NullWriter();
//        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job job = Job.run(reader, writer);
        System.out.println("Records Transferred: " + job.getRecordsTransferred());
        System.out.println("Running Time: " + job.getRunningTimeAsString());
    }
}
