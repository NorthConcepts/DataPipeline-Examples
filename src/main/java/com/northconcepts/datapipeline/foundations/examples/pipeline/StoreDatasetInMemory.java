package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;

import java.io.File;

public class StoreDatasetInMemory {

    public static void main(String[] args) {
        Pipeline pipeline = new Pipeline()
                .setInputAsDataReaderFactory(() -> new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                        .setFieldNamesInFirstRow(true));
        
        Dataset dataset = new MemoryDataset(pipeline);
        
        dataset.load().waitForRecordsToLoad();

        dataset.close();
    }
    
}
