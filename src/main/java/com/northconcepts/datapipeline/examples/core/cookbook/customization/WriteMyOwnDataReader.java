/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook.customization;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;

public class WriteMyOwnDataReader {
    
    public static void main(String[] args) throws Throwable {
        DataReader reader = new MyDataReader();
        reader = new MyProxyReader(reader);
        Job.run(reader, new StreamWriter(System.out));
    }
    
}
