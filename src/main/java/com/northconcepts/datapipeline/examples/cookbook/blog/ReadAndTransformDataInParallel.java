package com.northconcepts.datapipeline.examples.cookbook.blog;

import com.northconcepts.datapipeline.core.AsyncTaskReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.SortingReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.RenameField;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;

public class ReadAndTransformDataInParallel {

    public static void main(String[] args) {

        DataReader input = new CSVReader(new File("example/data/input/credit-balance-02-1000000.csv"))
            .setFieldNamesInFirstRow(true);

        DataReader reader = new AsyncTaskReader(input, (originalDataReader) ->
            new TransformingReader(originalDataReader)
                .add(new RenameField("Rating", "AccountRating"))
                .add(new BasicFieldTransformer("Balance").stringToDouble().nullToValue(0.0))
            , 4);

        Job.run(new SortingReader(reader).add("Balance"), StreamWriter.newSystemOutWriter());
    }
}

