package com.northconcepts.datapipeline.examples.pdf;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.pdf.PdfDocument;
import com.northconcepts.datapipeline.pdf.PdfReader;

import java.io.File;

public class ReadAPdfFile {
    public static void main(String[] args) {
        File file = new File("example/data/input/addresses.pdf");
        PdfReader reader = new PdfReader(new PdfDocument(file));

        Job.run(reader, new StreamWriter(System.out));
    }
}
