/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.orc;

import java.io.File;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.orc.OrcDataReader;

public class ReadAnOrcFile {

    public static void main(String[] args) {
        OrcDataReader reader = new OrcDataReader(new File("example/data/input/input_orc_file.orc"));
        Job.run(reader, new StreamWriter(System.out));

        System.out.println("============================================================");
        System.out.println("ORC Schema");
        System.out.println("============================================================");
        
        System.out.println(reader.getSchema());
    }

}
