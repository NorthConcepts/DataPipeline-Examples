package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.filter.Filter;
import com.northconcepts.datapipeline.filter.FilteringReader;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;

public class UsePredicatesInFilters {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        FilteringReader filteringReader = new FilteringReader(reader);
        filteringReader.add(Filter.of(record -> record.getField("Rating").getValue().equals("A")));

        DataWriter writer = new StreamWriter(System.out);

        Job.run(filteringReader, writer);
    }
}
