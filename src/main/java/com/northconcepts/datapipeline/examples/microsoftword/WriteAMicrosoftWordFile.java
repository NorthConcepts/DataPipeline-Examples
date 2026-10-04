package com.northconcepts.datapipeline.examples.microsoftword;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.microsoft.word.MicrosoftWordWriter;
import com.northconcepts.datapipeline.microsoft.word.MicrosoftWordWriter.PageOrientation;
import com.northconcepts.datapipeline.microsoft.word.MicrosoftWordWriter.PageSize;

public class WriteAMicrosoftWordFile {

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        DataWriter writer = new MicrosoftWordWriter(new File("example/data/output/credit-balance-01.docx"))
                .setPageSize(PageSize.A4)
                .setPageOrientation(PageOrientation.LANDSCAPE);

        Job.run(reader, writer);
    }

}
