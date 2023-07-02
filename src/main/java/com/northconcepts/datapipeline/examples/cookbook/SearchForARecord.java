package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filter.FilterExpression;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryWriter;

public class SearchForARecord {

    public static final Logger log = DataEndpoint.log;

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        MemoryWriter memoryWriter = new MemoryWriter();

        Job.run(reader, memoryWriter);

        int index = memoryWriter.getRecordList().findFirst(
                new FilterExpression("Rating == 'B' && parseDouble(CreditLimit) > 1000"), 0);

        // this would work too: "Rating = 'B' and parseDouble(CreditLimit) > 1000"

        if (index >= 0) {
            log.info(memoryWriter.getRecordList().get(index));
        } else {
            log.info("no record found");
        }

    }

}
