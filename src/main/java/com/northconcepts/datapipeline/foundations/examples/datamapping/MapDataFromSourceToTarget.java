package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingResult;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.foundations.datamapping.IFieldMapping;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;

public class MapDataFromSourceToTarget {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("fname", "John");
        input.setValue("lname", "Smith");

        // Create an instance of FieldMapping and provide targetFieldName & sourceExpression.
        // This same IFieldMapping can be used to other DataMapping if required.
        IFieldMapping nameFieldMapping = new FieldMapping("name", "source.fname + ' ' + target.last_name");
        
        // Create a collection of IFieldMapping. This same collection can be use to other DataMapping. This will increase reusability.
        List<IFieldMapping> fieldMappingList = new ArrayList<>();
        fieldMappingList.add(new FieldMapping("first_name", "source.fname"));
        fieldMappingList.add(new FieldMapping("last_name", "toUpperCase(source.lname)"));
        
        DataMapping mapping = new DataMapping()
//                a collection as parameter.
                .addFieldMapping(fieldMappingList)
                // IFieldMapping as parameter.
                .addFieldMapping(nameFieldMapping)
                // targetFieldName & sourceExpression as parameters and FieldMapping instance will be created.
                .addFieldMapping("name_length", "length(target.name)");
        
        DataMappingResult result = mapping.map(input);
        Record target = result.getTarget();

        System.out.println("target = " + target);
        
        result.logExceptions();
    }

}
