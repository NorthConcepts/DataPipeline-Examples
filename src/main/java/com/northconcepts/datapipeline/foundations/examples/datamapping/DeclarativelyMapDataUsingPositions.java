package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.AsyncReader;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;
import java.io.FileInputStream;

public class DeclarativelyMapDataUsingPositions {

    public static void main(String... args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-02-100000-no-header.csv"))
                .setFieldNamesInFirstRow(false);
        
        SchemaDef schema = (SchemaDef) new SchemaDef()
                .fromXml(new FileInputStream(new File("example/data/input/datamapping/account-schema-definition-position.xml")));
        EntityDef sourceAccountEntity = schema.getEntity("SourceAccountEntity");
        reader = new TransformingReader(reader)
                .add(new SchemaTransformer(sourceAccountEntity));
        
//        reader = new AsyncReader(reader);  // Threading

        DataMapping mapping = (DataMapping) new DataMapping()
                .fromXml(new FileInputStream("example/data/input/datamapping/credit-balance-mapping-2.xml"));
        reader = new DataMappingReader(reader, mapping);

//        DataWriter writer = new NullWriter();
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job job = Job.run(reader, writer);
        System.out.println("Records Transferred: " + job.getRecordsTransferred());
        System.out.println("Running Time: " + job.getRunningTimeAsString());
    }
}
