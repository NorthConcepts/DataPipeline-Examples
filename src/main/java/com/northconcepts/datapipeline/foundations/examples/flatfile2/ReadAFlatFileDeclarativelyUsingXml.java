package com.northconcepts.datapipeline.foundations.examples.flatfile2;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.core.XmlSerializable;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;

public class ReadAFlatFileDeclarativelyUsingXml {

    public static void main(String[] args) throws Throwable {
        FlatFilePipelineInput input = XmlSerializable.newInstanceFromXml(ReadAFlatFileDeclarativelyUsingJson.class.getResourceAsStream("ReadAFlatFileDeclaratively.xml"));

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
