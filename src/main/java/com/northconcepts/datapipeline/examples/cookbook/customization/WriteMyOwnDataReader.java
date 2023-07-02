package com.northconcepts.datapipeline.examples.cookbook.customization;

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
