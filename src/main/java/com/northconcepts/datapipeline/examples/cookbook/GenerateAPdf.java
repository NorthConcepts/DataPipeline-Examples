/*
 * Copyright (c) 2006-2018 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 *
 * http://northconcepts.com/data-pipeline/licensing/
 *
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.pdf.PdfWriter;
import com.northconcepts.datapipeline.xml.SimpleXmlReader;

public class GenerateAPdf {

    public static void main(String[] args) {
        DataReader reader = new SimpleXmlReader(new File("example/data/input/simple-xml-input.xml"));
        DataWriter writer = new PdfWriter(new File("example/data/output/simple-xml-output.pdf"));

        Job.run(reader, writer);
    }

}
