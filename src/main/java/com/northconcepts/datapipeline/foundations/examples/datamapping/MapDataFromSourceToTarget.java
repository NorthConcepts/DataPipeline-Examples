package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingResult;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class MapDataFromSourceToTarget {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("fname", "John");
        input.setValue("lname", "Smith");

        DataMapping mapping = new DataMapping()
                .addFieldMapping(new FieldMapping("first_name", "source.fname"))
                .addFieldMapping(new FieldMapping("last_name", "toUpperCase(source.lname)"))
                .addFieldMapping(new FieldMapping("name", "source.fname + ' ' + target.last_name"))
                .addFieldMapping(new FieldMapping("name_length", "length(target.name)"));
        
        DataMappingResult result = mapping.map(input);
        Record target = result.getTarget();

        System.out.println("target = " + target);
        
        result.logExceptions();
    }

}
