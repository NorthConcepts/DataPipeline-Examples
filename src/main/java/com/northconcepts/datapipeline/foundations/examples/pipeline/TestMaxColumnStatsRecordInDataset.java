/*******************************************************************************
 * Removed_dp_license
 ******************************************************************************/
package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Column;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MvStoreDataset;

public class TestMaxColumnStatsRecordInDataset {
    private static final File DATABASE_FOLDER = new File("example/data/output", "mvstore-dataset");

    public static void main(String[] args) throws Throwable {
        DataReader reader = new CSVReader(
                new File("D:\\git\\DataPipeline-Examples\\example\\data\\input\\1000_Sales_Records.csv"))
                        .setFieldNamesInFirstRow(true);
        Pipeline pipeline = new Pipeline().setInputAsDataReader(reader);
        File databaseFile = new File(DATABASE_FOLDER, "StoreDatasetOnDisk.mvstore");
        if (databaseFile.exists()) {
            Files.delete(databaseFile.toPath());
        }
        Dataset dataset = MvStoreDataset.createDataset(databaseFile, pipeline);
        dataset.setColumnStatsReaderThreads(1);
        dataset.setMaxColumnStatsRecords(100L);
        dataset.load();
        dataset.waitForRecordsToLoad();
        dataset.waitForColumnStatsToLoad(900, 10000);
        
        
        printColumns(dataset);
        System.out.println("dataset.getRecordCount(): " + dataset.getRecordCount());
        System.out.println("dataset.getColumnCount(): " + dataset.getColumnCount());
        dataset.close();
    }

    private static void printColumns(Dataset dataset) {
        List<Column> columns = dataset.getColumns();
        for (Column column : columns) {
            System.out.println("=============================== column name: " + column.getName()
                    + " =======================================");
            System.out.println("column: " + column);
        }
    }
}
