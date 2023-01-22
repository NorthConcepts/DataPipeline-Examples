package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.AsyncTaskReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.SortingReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.DataReaderDecorator;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class ReadAndTransformDataInParallel {

    private static final int THREADS = 4;

    public static void main(String[] args) {

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02-1000000.csv"))
                .setFieldNamesInFirstRow(true);

        reader = new AsyncTaskReader(reader, createTaskToRunInMultipleThreads(), THREADS);

        Job.run(new SortingReader(reader).add("Balance"), StreamWriter.newSystemOutWriter());
    }

    private static DataReaderDecorator createTaskToRunInMultipleThreads() {
        return (originalDataReader) -> new TransformingReader(originalDataReader)
                .add(new RenameField("Rating", "AccountRating"))
                .add(new BasicFieldTransformer("Balance").stringToDouble().nullToValue(0.0));
    }

}

