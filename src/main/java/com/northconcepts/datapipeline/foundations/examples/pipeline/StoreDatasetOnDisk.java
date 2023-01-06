package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MvStoreDataset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StoreDatasetOnDisk {
    private static final File DATABASE_FOLDER = new File("example/data/output", "mvstore-dataset");

    public static void main(String[] args) throws IOException {
        Pipeline pipeline = new Pipeline()
                .setInputAsDataReaderFactory(() -> new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                        .setFieldNamesInFirstRow(true));

        File databaseFile = new File(DATABASE_FOLDER, "testCreateDataReader.mvstore");

        if (databaseFile.exists()) {
            Files.delete(databaseFile.toPath());
        }

        Dataset dataset = MvStoreDataset.createDataset(databaseFile, pipeline);

        try {
            dataset.setColumnStatsReaderThreads(1);
            dataset.load();
            dataset.waitForColumnStatsToLoad();
        } catch (Throwable e) {
            System.out.println(e);
        }

        dataset.close();

    }

}
