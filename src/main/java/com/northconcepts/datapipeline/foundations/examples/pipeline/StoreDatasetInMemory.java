package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;

import java.io.File;

public class StoreDatasetInMemory {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline().setInputAsDataReader(reader);

        Dataset dataset = new MemoryDataset(pipeline);

        dataset.load().waitForRecordsToLoad();

        for (Record record : dataset) {
            System.out.println(record);
        }
        dataset.close();
    }

}
