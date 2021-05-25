package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DeclarativelyTransformRecordsUsingSchema {

    public static void main(String... args) throws FileNotFoundException {
        SchemaDef schema = new SchemaDef()
            .fromXml(new FileInputStream("example/data/input/datamapping/account-schema-definition.xml"));
        EntityDef sourceAccountEntity = schema.getEntity("SourceAccountEntity");

        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02b.csv"))
            .setFieldNamesInFirstRow(true);

        reader = new TransformingReader(reader)
            .add(new SchemaTransformer(sourceAccountEntity));

        DataWriter writer = new NullWriter();

        Job job = Job.run(reader, writer);
        System.out.println("Records Transferred: " + job.getRecordsTransferred());
        System.out.println("Running Time: " + job.getRunningTimeAsString());
    }
}
