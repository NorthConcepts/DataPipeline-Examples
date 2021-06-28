/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;

public class SetDefaultValuesFoMissingData {

    public static void main(String... args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/schema/csv-with-null-values.csv"))
                .setFieldNamesInFirstRow(true);
        
        SchemaDef schema = new SchemaDef()
            .addEntity(new EntityDef().setName("Entity")
                .addField(new NumericFieldDef().setName("ID").setType(FieldType.INT)
                    .setDefaultValueExpression("0"))
                .addField(new BooleanFieldDef().setName("BOOLEAN1")
                    .setDefaultValueExpression("false"))
                .addField(new TextFieldDef().setName("STRING1").setType(FieldType.STRING)
                    .setDefaultValueExpression("'DEFAULT STRING VALUE'"))
                .addField(new NumericFieldDef().setName("BYTE1").setType(FieldType.BYTE)
                    .setDefaultValueExpression("88"))
                .addField(new TextFieldDef().setName("CHAR1").setType(FieldType.CHAR)
                    .setDefaultValueExpression("'z'"))
                .addField(new TemporalFieldDef().setName("DATE1").setType(FieldType.DATE)
                    .setDefaultValueExpression("toDate('1999-08-21T15:31:50.000Z')"))
                .addField(new TemporalFieldDef().setName("DATETIME1").setType(FieldType.DATETIME)
                    .setDefaultValueExpression("toDatetime('1999-08-21T15:31:50.000Z')"))
                .addField(new TemporalFieldDef().setName("TIME1").setType(FieldType.TIME)
                    .setDefaultValueExpression("toDatetime('1999-08-21T15:31:50.000Z')"))
                .addField(new NumericFieldDef().setName("INT1").setType(FieldType.INT)
                    .setDefaultValueExpression("99"))
                .addField(new NumericFieldDef().setName("LONG1").setType(FieldType.LONG)
                    .setDefaultValueExpression("1152921504606846976"))
                .addField(new NumericFieldDef().setName("DOUBLE1").setType(FieldType.DOUBLE)
                    .setDefaultValueExpression("10.0"))
                .addField(new NumericFieldDef().setName("FLOAT1").setType(FieldType.FLOAT)
                    .setDefaultValueExpression("99"))
                .addField(new NumericFieldDef().setName("SHORT1").setType(FieldType.SHORT)
                    .setDefaultValueExpression("99"))
                .addField(new NumericFieldDef().setName("BIGDECIMAL1").setType(FieldType.BIG_DECIMAL)
                    .setDefaultValueExpression("'111111111111111111111111111111111111111.111111111111111111111111111111111111111'"))
                .addField(new NumericFieldDef().setName("BIGINTEGER1").setType(FieldType.BIG_INTEGER)
                    .setDefaultValueExpression("'99999999999999999999999999999999999999'"))
            );


        EntityDef entity = schema.getEntity("Entity");
        reader = new TransformingReader(reader)
                .add(new SchemaTransformer(entity));

//        DataWriter writer = new NullWriter();
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job job = Job.run(reader, writer);
        System.out.println("Records Transferred: " + job.getRecordsTransferred());
        System.out.println("Running Time: " + job.getRunningTimeAsString());
    }
}
