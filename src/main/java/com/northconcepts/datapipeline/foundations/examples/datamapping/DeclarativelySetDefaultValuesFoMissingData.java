/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;
import java.io.FileInputStream;

public class DeclarativelySetDefaultValuesFoMissingData {

    public static void main(String... args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/schema/csv-with-null-values.csv"))
                .setFieldNamesInFirstRow(true);
        
        SchemaDef schema = new SchemaDef()
                .fromXml(new FileInputStream(new File("example/data/input/schema/schema-with-default-values.xml")));
        EntityDef sourceAccountEntity = schema.getEntity("Entity");
        reader = new TransformingReader(reader)
                .add(new SchemaTransformer(sourceAccountEntity));

//        DataWriter writer = new NullWriter();
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job job = Job.run(reader, writer);
        System.out.println("Records Transferred: " + job.getRecordsTransferred());
        System.out.println("Running Time: " + job.getRunningTimeAsString());
    }
}
