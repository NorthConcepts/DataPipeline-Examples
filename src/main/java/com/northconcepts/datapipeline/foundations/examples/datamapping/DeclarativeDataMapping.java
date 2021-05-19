package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DeclarativeDataMapping {

    public static void main(String... args) throws Throwable {
        File schemaXmlFile = new File("example/data/input/account-schema-definition.xml");
        SchemaDef schema = new SchemaDef().fromXml(new FileInputStream(schemaXmlFile));
        EntityDef entityDef = schema.getEntity("Account");

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02.csv"))
            .setFieldNamesInFirstRow(true);

        InputStream stream = new FileInputStream("example/data/input/credit-balance-mapping.xml");

        DataMapping mapping = new DataMapping();
        mapping.setTargetValidationEntity(entityDef);
        mapping.fromXml(stream);

        reader = new DataMappingReader(reader, mapping);

        Job.run(reader, StreamWriter.newSystemOutWriter());
    }
}
