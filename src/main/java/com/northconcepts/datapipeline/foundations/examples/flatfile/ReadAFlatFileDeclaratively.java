package com.northconcepts.datapipeline.foundations.examples.flatfile;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
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

        System.out.println("---------------------");
        System.out.println(input.toXml());
        System.out.println("---------------------");
        System.out.println(Util.formatJson(input.toJson()));
        System.out.println("---------------------");

        DataReader reader = input.createDataReader();
        DataWriter writer = StreamWriter.newSystemOutWriterWithSessionProperties();

        Job.run(reader, writer);
    }

}
