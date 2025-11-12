package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.job.Job;

import org.apache.log4j.Logger;

public class WriteToAnExcelFileWithFreezePane {

    public static final Logger log = DataEndpoint.log;

    public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/patient-visits-raw-10000.csv"))
                .setFieldNamesInFirstRow(true);

        ExcelDocument document = new ExcelDocument();
        DataWriter writer = new ExcelWriter(document).setSheetName("balance")
                .setFreezeRows(1) // freeze pane is applied to the first row only.
                .setFreezeColumns(2); // freeze pane is applied to the first two columns.

        Job.run(reader, writer);
        
        document.save(new File("example/data/output/patient-visits-with-freeze-pane.xlsx"));
    }

}
