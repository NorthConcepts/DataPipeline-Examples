package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingResult;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class MapDataFromSourceToTarget {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("fname", "John");
        input.setValue("lname", "Smith");

        DataMapping mapping = new DataMapping()
                .addFieldMapping("first_name", "source.fname")
                .addFieldMapping("last_name", "toUpperCase(source.lname)")
                .addFieldMapping("name", "source.fname + ' ' + target.last_name")
                .addFieldMapping("name_length", "length(target.name)");
        
        DataMappingResult result = mapping.map(input);
        Record target = result.getTarget();

        System.out.println("target = " + target);
        
        result.logExceptions();
    }

}
