package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingResult;
import com.northconcepts.datapipeline.foundations.datamapping.FieldMapping;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;
import com.northconcepts.datapipeline.transform.lookup.BasicLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;

public class MapDataFromSourceToTargetWithLookup {

    public static void main(String[] args) {
        DefaultExpressionContext input = new DefaultExpressionContext();
        input.setValue("fname", "John");
        input.setValue("lname", "Smith");
        input.setValue("currency_code", "mxn");

        Lookup currencyLookup = new BasicLookup(new FieldList("Currency Name"))
                .add("CAD", "Canadian Dollar")
                .add("USD", "American Dollar")
                .add("EUR", "Euro")
                .add("GBP", "British Pound")
                .add("MXN", "Mexican Peso")
                ;
        
        DataMapping mapping = new DataMapping()
                .setValue("currencyLookup", currencyLookup)
                .addFieldMapping(new FieldMapping("first_name", "source.fname"))
                .addFieldMapping(new FieldMapping("currency", "lookup(0, currencyLookup, toUpperCase(source.currency_code))"))
                ;
        
        DataMappingResult result = mapping.map(input);
        Record target = result.getTarget();
        System.out.println("target = " + target);
        
        result.logExceptions();

    }

}
