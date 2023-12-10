package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.foundations.tools.GenerateRecordSerializers;

import java.util.HashSet;

public class GenerateRecordSerializersExample {
    public static void main(String[] args) throws Throwable {
        GenerateRecordSerializers serializers = new GenerateRecordSerializers();
        HashSet<String> types = new HashSet<>();
        types.add("com.northconcepts.datapipeline.foundations.datamapping.DataMapping");
        types.add("com.northconcepts.datapipeline.foundations.pipeline.input.CsvPipelineInput");
        serializers.generate(types);
    }
}
