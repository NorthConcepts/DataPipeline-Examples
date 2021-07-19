package com.northconcepts.datapipeline.foundations.examples.flatfile2;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.file.LocalFileSource;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;

public class ReadAFlatFileDeclaratively {

    public static void main(String[] args) throws Throwable {
        FlatFilePipelineInput input = new FlatFilePipelineInput()
                .setFileSource(new LocalFileSource().setPath("example/data/input/flatfile-01.txt"))
                .addFixedLengthField(new TextFieldDef("id", FieldType.STRING), 10)
                .addFixedLengthField(new NumericFieldDef("year", FieldType.INT), 4)
                .addFixedLengthField(new NumericFieldDef("month", FieldType.INT), 2)
                .addFixedLengthField(new NumericFieldDef("day", FieldType.INT), 2)
                .addVariableLengthField(new TextFieldDef("firstName", FieldType.STRING), "@@")
                .addVariableLengthField(new TextFieldDef("lastName", FieldType.STRING), "!")
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
