package com.northconcepts.datapipeline.foundations.examples.datamapping;

import java.io.File;
import java.io.FileInputStream;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
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

public class DeclarativelyMapData {

    public static void main(String... args) throws Throwable {
        SchemaDef schema = new SchemaDef()
                .fromXml(new FileInputStream(new File("example/data/input/datamapping/account-schema-definition.xml")));
        EntityDef sourceAccountEntity = schema.getEntity("SourceAccountEntity");
        EntityDef targetAccountEntity = schema.getEntity("TargetAccountEntity");

        Lookup statusLookup = new BasicLookup(new FieldList("status"))
            .add("A", "Updated")
            .add("B", "Late")
            .add("C", "Overdue")
            .add("D", "Default");

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02.csv"))
                .setFieldNamesInFirstRow(true);
        
        reader = new TransformingReader(reader).add(new SchemaTransformer(sourceAccountEntity));
        
        DataMapping mapping = new DataMapping()
                .fromXml(new FileInputStream("example/data/input/datamapping/credit-balance-mapping.xml"));
        mapping.setTargetValidationEntity(targetAccountEntity);
        mapping.setValue("statusLookup", statusLookup);

        reader = new DataMappingReader(reader, mapping);

        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job.run(reader, writer);
    }
}
