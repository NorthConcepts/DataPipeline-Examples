package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MvStoreDataset;

import java.io.File;
import java.nio.file.Files;

public class StoreDatasetOnDisk {
    private static final File DATABASE_FOLDER = new File("example/data/output", "mvstore-dataset");

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);
        
        Pipeline pipeline = new Pipeline().setInputAsDataReader(reader);

        File databaseFile = new File(DATABASE_FOLDER, "StoreDatasetOnDisk.mvstore");

        if (databaseFile.exists()) {
            Files.delete(databaseFile.toPath());
        }

        Dataset dataset = MvStoreDataset.createDataset(databaseFile, pipeline);

        dataset.setColumnStatsReaderThreads(1);
        dataset.load();
        dataset.waitForColumnStatsToLoad();

        dataset.close();
        
        dataset = MvStoreDataset.openDataset(databaseFile);

        System.out.println(dataset.getRecord(0));

    }

}
