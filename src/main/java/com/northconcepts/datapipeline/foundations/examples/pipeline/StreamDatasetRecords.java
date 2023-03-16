package com.northconcepts.datapipeline.foundations.examples.pipeline;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.pipeline.Pipeline;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.Dataset;
import com.northconcepts.datapipeline.foundations.pipeline.dataset.MemoryDataset;
import com.northconcepts.datapipeline.internal.lang.Util;

public class StreamDatasetRecords {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        Pipeline pipeline = new Pipeline().setInputAsDataReader(reader);

        Dataset dataset = new MemoryDataset(pipeline);

        dataset.load().waitForRecordsToLoad();

        System.out.println("================================Using forEach================================");
        dataset.forEach(record -> System.out.println(record));

        System.out.println("\n\n================================Using stream================================");
        dataset.stream()
                .map(record -> Util.formatXml(record.toXml()))
                .forEach(recordAsXml -> System.out.println(recordAsXml));
    }
}
