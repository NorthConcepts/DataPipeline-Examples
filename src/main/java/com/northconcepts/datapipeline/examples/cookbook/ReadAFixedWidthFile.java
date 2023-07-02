package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.fixedwidth.FixedWidthReader;

public class ReadAFixedWidthFile {
    
    public static final Logger log = DataEndpoint.log; 

    public static void main(String[] args) {
        FixedWidthReader reader = new FixedWidthReader(new File("example/data/input/credit-balance-01.fw"));

        reader.setFieldNamesInFirstRow(true);
        
        reader.addFields(8);
        reader.addFields(16);
        reader.addFields(16);
        reader.skipField(12);  // ignore field 3 - Balance
        reader.addFields(14);
        reader.addFields(16);
        reader.addFields(7);

        reader.open();
        try {
            Record record;
            while ((record = reader.read()) != null) {
                log.info(record);
            }
        } finally {
            reader.close();
        }
    }
}
