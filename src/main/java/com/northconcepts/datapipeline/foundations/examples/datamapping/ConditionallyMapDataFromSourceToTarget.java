package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingResult;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class ConditionallyMapDataFromSourceToTarget {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("fname", "John");
        input.setValue("lname", "Smith");

        DataMapping mapping = new DataMapping()
                .addCondition("length(source.fname) > 3") // don't map anything if fname is not greater than 3 characters
                .addFieldMapping("first_name", "source.fname")
                .addFieldMapping("length", "length(source.fname)");
        
        DataMappingResult result = mapping.map(input);
        Record target = result.getTarget();

        System.out.println("target = " + target);
        
        result.logExceptions();
    }

}
