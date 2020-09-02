/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.core.cookbook;

import java.io.File;
import java.io.FileWriter;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.template.TemplateWriter;

public class WriteHtmlUsingFreeMarkerTemplates {

    /*
     * Produces the following HTML
     * 
     * <table> <thead> <tr> <td>stageName</td> <td>realName</td> <td>gender</td> <td>city</td> <td>balance</td> </tr> </thead> <tbody> <tr>
     * <td>John Wayne</td> <td>Marion Robert Morrison</td> <td>male</td> <td>Winterset</td> <td>156.35</td> </tr> <tr> <td>Spiderman</td>
     * <td>Peter Parker</td> <td>male</td> <td>New York</td> <td>-0.96</td> </tr> </tbody> </table>
     * 
     */

    public static void main(String[] args) throws Throwable {

        Record record1 = new Record();
        record1.getField("stageName", true).setValue("John Wayne");
        record1.getField("realName", true).setValue("Marion Robert Morrison");
        record1.getField("gender", true).setValue("male");
        record1.getField("city", true).setValue("Winterset");
        record1.getField("balance", true).setValue(156.35);

        Record record2 = new Record();
        record2.getField("stageName", true).setValue("Spiderman");
        record2.getField("realName", true).setValue("Peter Parker");
        record2.getField("gender", true).setValue("male");
        record2.getField("city", true).setValue("New York");
        record2.getField("balance", true).setValue(-0.96);

        MemoryReader reader = new MemoryReader(new RecordList(record1, record2));

        TemplateWriter writer = new TemplateWriter(new FileWriter("example/data/output/credit-balance-04.html"));
        writer.setFieldNamesInFirstRow(false);
        writer.getConfiguration().setDirectoryForTemplateLoading(new File("example/data/input"));
        // writer.setHeaderTemplate("WriteHtmlUsingFreeMarkerTemplates-header.html"); // No header in this example
        writer.setFooterTemplate("WriteHtmlUsingFreeMarkerTemplates-footer.html");
        writer.setDetailTemplate("WriteHtmlUsingFreeMarkerTemplates-detail.html");

        Job.run(reader, writer);
    }

}
