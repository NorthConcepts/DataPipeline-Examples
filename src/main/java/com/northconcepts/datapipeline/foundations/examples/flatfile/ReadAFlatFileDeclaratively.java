/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.flatfile;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.pipeline.DataMappingPipeline;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;

public class ReadAFlatFileDeclaratively {

    public static void main(String[] args) throws Throwable {
        FlatFilePipelineInput input = new FlatFilePipelineInput()
                .setFileSource(new LocalFileSource().setPath("example/data/input/flatfile-01.txt"))
                .addFixedLengthField("id", 10)
                .addFixedLengthField("year", 4)
                .addFixedLengthField("month", 2)
                .addFixedLengthField("day", 2)
                .addVariableLengthField("firstName", "@@")
                .addVariableLengthField("lastName", "!")
                .setSaveLineage(true);

        EntityDef entity = new EntityDef()
                .addField(new TextFieldDef("id", FieldType.STRING))
                .addField(new NumericFieldDef("year", FieldType.INT).setMinimum(2020))
                .addField(new NumericFieldDef("month", FieldType.INT).setMinimum(1).setMaximum(12))
                .addField(new NumericFieldDef("day", FieldType.INT).setMinimum(1).setMaximum(31))
                .addField(new TextFieldDef("firstName", FieldType.STRING))
                .addField(new TextFieldDef("lastName", FieldType.STRING));

        DataMappingPipeline pipeline = new DataMappingPipeline();
        pipeline.setInput(input);
        pipeline.setSourceEntity(entity);

        System.out.println("---------------------");
        System.out.println(pipeline.toXml());
        System.out.println("---------------------");
        System.out.println(Util.formatJson(pipeline.toJson()));
        System.out.println("---------------------");


        DataReader reader = pipeline.createDataReader();
        DataWriter writer = StreamWriter.newSystemOutWriterWithSessionProperties();

        Job.run(reader, writer);
    }

}
