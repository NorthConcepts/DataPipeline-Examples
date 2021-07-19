package com.northconcepts.datapipeline.foundations.examples.flatfile;

import java.io.FileReader;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;

public class ReadAFlatFile {

    public static void main(String[] args) throws Throwable {
        FlatFileReader reader = new FlatFileReader(new FileReader("example/data/input/flatfile-01.txt"))
                .addFixedLengthField("id", 10)
                .addFixedLengthField("year", 4)
                .addFixedLengthField("month", 2)
                .addFixedLengthField("day", 2)
                .addVariableLengthField("firstName", "@@")
                .addVariableLengthField("lastName", "!")
                ;
        DataWriter writer = StreamWriter.newSystemOutWriterWithSessionProperties();

        Job.run(reader, writer);
    }

}
