package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;

public class ReadAllTabsFromAnExcelFile {

    public static void main(String[] args) throws Throwable {
        ExcelDocument document = new ExcelDocument().open(new File("example/data/input/data-with-multiple-tabs.xlsx"));

        for (String sheetName : document.getSheetNames()) {
            DataReader reader = new ExcelReader(document).setFieldNamesInFirstRow(true).setSheetName(sheetName);
            System.out.println("SheetName:"+sheetName);
            
            Job.run(reader, new StreamWriter(System.out));
        }

    }
}
