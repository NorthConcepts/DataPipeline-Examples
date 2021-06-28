/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.FileWriter;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.xml.XmlWriter;
import com.northconcepts.datapipeline.xml.builder.XmlElement;
import com.northconcepts.datapipeline.xml.builder.XmlNodeContainer;
import com.northconcepts.datapipeline.xml.builder.XmlTemplate;

public class WriteAnXmlFileProgrammatically {

    /*
     * Produces the following XML (line breaks added for clarity)
     * 
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?> <actors> <north-america> <actor stage-name="John Wayne" real-name=
     * "Marion Robert Morrison"> <gender>male</gender> <city>Winterset</city> <balance>156.35</balance> </actor> <actor
     * stage-name="Spiderman" real-name="Peter Parker"> <gender>male</gender> <city>New York</city> </actor> </north-america> </actors>
     * 
     */

    public static void main(String[] args) throws Throwable {

        Record record1 = new Record();
        record1.setField("stageName", "John Wayne");
        record1.setField("realName", "Marion Robert Morrison");
        record1.setField("gender", "male");
        record1.setField("city", "Winterset");
        record1.setField("balance", 156.35);

        Record record2 = new Record();
        record2.getField("stageName", true).setValue("Spiderman");
        record2.getField("realName", true).setValue("Peter Parker");
        record2.getField("gender", true).setValue("male");
        record2.getField("city", true).setValue("New York");
        record2.getField("balance", true).setValue(-0.96);

        MemoryReader reader = new MemoryReader(new RecordList(record1, record2));

        // create the XML template
        // arguments to element(), attribute(), text(), and when() are expressions
        // literal strings must be quoted
        XmlTemplate template = new XmlTemplate();
        template.setDeclaration("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");

        // detail() marks the node where records are added
        XmlNodeContainer detailElement = template.element("'actors'").element("'north-america'").detail();

        XmlElement actor = detailElement.element("'actor'");
        actor.attribute("'stage-name'", "stageName").attribute("'real-name'", "realName");
        actor.element("'gender'").text("gender");
        actor.element("'city'").text("city");
        actor.when("balance >= 0").element("'balance'").text("balance"); // add this branch when balance is not negative

        // add the template to the writer
        XmlWriter writer = new XmlWriter(template, new FileWriter("example/data/output/credit-balance-03.xml"))
                .setPretty(true);

        Job.run(reader, writer);
    }

}
