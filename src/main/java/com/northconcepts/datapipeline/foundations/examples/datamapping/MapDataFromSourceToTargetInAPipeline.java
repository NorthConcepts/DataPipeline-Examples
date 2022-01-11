/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.job.Job;

public class MapDataFromSourceToTargetInAPipeline {

    public static void main(String[] args) {
        DataMapping mapping = new DataMapping()
                .addFieldMapping(new FieldMapping("Title", "coalesce(source.Title, source.Handle)"))
                .addFieldMapping(new FieldMapping("Cost", "${source.Variant Price}"))  // use ${} since field has space in name
                .addFieldMapping(new FieldMapping("Price", "toBigDecimal(target.Cost) + 10.00"));
        
        DataReader reader = new CSVReader(new File("data/input/jewelry.csv"))
                .setAllowMultiLineText(true)
                .setFieldNamesInFirstRow(true);
        
        reader = new DataMappingReader(reader, mapping);
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }

}
