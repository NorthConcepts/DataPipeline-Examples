package com.northconcepts.datapipeline.examples.openfinancialexchange;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.openfinancialexchange.OpenFinancialExchangeReader;

public class ReadAnOfxFile {

    public static void main(String[] args) {
        DataReader reader = new OpenFinancialExchangeReader(new File("example/data/input/bank-statement.ofx"));

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }

}
