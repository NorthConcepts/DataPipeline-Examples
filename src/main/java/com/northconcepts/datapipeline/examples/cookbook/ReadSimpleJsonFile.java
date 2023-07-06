package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonReader;

public class ReadSimpleJsonFile {

    public static void main(String[] args) {
        DataReader reader = new SimpleJsonReader(new File("example/data/input/simple-json-input.json"));
        
        Job.run(reader, new StreamWriter(System.out));
    }
}
