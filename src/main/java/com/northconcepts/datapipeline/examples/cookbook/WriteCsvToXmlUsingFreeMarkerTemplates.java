package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.io.FileWriter;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.template.TemplateWriter;

public class WriteCsvToXmlUsingFreeMarkerTemplates {

    /*
     * Produces the following XML
     * 
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?> <records> <record> <field name="stageName">John Wayne</field> <field
     * name="realName">Marion Robert Morrison</field> <field name="gender">male</field> <field name="city">Winterset</field> <field
     * name="balance">156.35</field> </record>
     * 
     * <record> <field name="stageName">Spiderman</field> <field name="realName">Peter Parker</field> <field name="gender">male</field>
     * <field name="city">New York</field> <field name="balance">-0.96</field> </record> </records>
     * 
     * 
     */

    public static void main(String[] args) throws Throwable {
        String in = "example/data/input/WriteCsvToXmlUsingFreeMarkerTemplates.csv";
        String out = "example/data/output/WriteCsvToXmlUsingFreeMarkerTemplates.xml";

        DataReader reader = new CSVReader(new File(in)).setFieldNamesInFirstRow(true);

        TemplateWriter writer = new TemplateWriter(new FileWriter(out));
        writer.setFieldNamesInFirstRow(false);
        writer.getConfiguration().setDirectoryForTemplateLoading(new File("example/data/input"));
        writer.setHeaderTemplate("WriteAnXmlFileUsingFreeMarkerTemplates-header-2.xml");
        writer.setFooterTemplate("WriteAnXmlFileUsingFreeMarkerTemplates-footer-2.xml");
        writer.setDetailTemplate("WriteAnXmlFileUsingFreeMarkerTemplates-detail-2.xml");

        Job.run(reader, writer);
    }

}
