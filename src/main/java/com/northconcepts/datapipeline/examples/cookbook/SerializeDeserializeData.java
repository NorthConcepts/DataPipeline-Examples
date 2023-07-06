package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.file.FileReader;
import com.northconcepts.datapipeline.file.FileWriter;
import com.northconcepts.datapipeline.job.Job;

public class SerializeDeserializeData {

    public static void main(String[] args) {
        File csvFile = new File("example/data/input/credit-balance-01.csv");
        File binaryFile = new File("example/data/output/credit-balance-01.bin");
        
        DataReader reader;
        DataWriter writer;

        // Serialize records to binary file
        reader = new CSVReader(csvFile).setFieldNamesInFirstRow(true);
        writer = new FileWriter(binaryFile);
        Job.run(reader, writer);
        
        // Deserialize records from binary file
        reader = new FileReader(binaryFile);
        writer = new StreamWriter(System.out);
        Job.run(reader, writer);
        
    }

}
