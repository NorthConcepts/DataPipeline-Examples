package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;
import com.northconcepts.datapipeline.transform.lookup.BasicLookup;
import com.northconcepts.datapipeline.transform.lookup.Lookup;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DeclarativeDataMapping {

    public static void main(String... args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02.csv"))
            .setFieldNamesInFirstRow(true);

        File schemaXmlFile = new File("example/data/input/datamapping/account-schema-definition.xml");
        SchemaDef schema = new SchemaDef().fromXml(new FileInputStream(schemaXmlFile));
        EntityDef sourceAccountEntity = schema.getEntity("SourceAccountEntity");
        EntityDef targetAccountEntity = schema.getEntity("TargetAccountEntity");

        InputStream stream = new FileInputStream("example/data/input/datamapping/credit-balance-mapping.xml");

        Lookup statusLookup = new BasicLookup(new FieldList("status"))
            .add("A", "Updated")
            .add("B", "Late")
            .add("C", "Overdue")
            .add("D", "Default");

        DataMapping mapping = new DataMapping();
        mapping.fromXml(stream);
        mapping.setTargetValidationEntity(targetAccountEntity);
        mapping.setValue("statusLookup", statusLookup);

        reader = new TransformingReader(reader).add(new SchemaTransformer(sourceAccountEntity));
        reader = new DataMappingReader(reader, mapping);

        Job.run(reader, StreamWriter.newSystemOutWriter());
    }
}
