package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonRecordReader;

public class ReadBigDecimalInJSON {

    public static void main(String[] args) {
        DataReader reader = new JsonRecordReader(new File("example/data/input/simple-json-to-file-with-bigdecimal.json"))
                .addRecordBreak("/array/object").setUseBigDecimal(true);
        
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }
}
