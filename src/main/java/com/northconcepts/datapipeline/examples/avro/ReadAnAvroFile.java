/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.avro;

import java.io.File;

import com.northconcepts.datapipeline.avro.AvroReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;

public class ReadAnAvroFile {
    
    public static void main(String[] args) {
        DataReader reader = new AvroReader(new File("example/data/input/twitter.avro"));
        
        Job.run(reader, new StreamWriter(System.out));
    }
/* output
-----------------------------------------------
0 - Record (MODIFIED) {
    0:[username]:STRING=[miguno]:String
    1:[tweet]:STRING=[Rock: Nerf paper, scissors is fine.]:String
    2:[timestamp]:LONG=[1366150681]:Long
}

-----------------------------------------------
1 - Record (MODIFIED) {
    0:[username]:STRING=[BlizzardCS]:String
    1:[tweet]:STRING=[Works as intended.  Terran is IMBA.]:String
    2:[timestamp]:LONG=[1366154481]:Long
}

-----------------------------------------------
2 records
*/
}
