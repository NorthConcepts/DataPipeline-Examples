/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.SequenceReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadASetOfFiles {
    
    private static final String INPUT = "example/data/input";
    private static final String[] FILES = {"countries-1.csv", "countries-2.csv"};

    public static void main(String[] args) {
        
        SequenceReader sequenceReader = new SequenceReader();
        
        for (int i = 0; i < FILES.length; i++) {
            sequenceReader.add(new CSVReader(new File(INPUT, FILES[i]))
                    .setFieldNamesInFirstRow(true));            
        }
        
        DataReader reader = sequenceReader;
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }

}
