package com.northconcepts.datapipeline.foundations.examples.datamapping;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;

public class SetDefaultValuesForMissingData {

    public static void main(String... args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/schema/csv-with-null-values.csv"))
                .setFieldNamesInFirstRow(true);
        
        SchemaDef schema = new SchemaDef()
            .addEntity(new EntityDef().setName("Entity")
                .addField(new NumericFieldDef("ID", FieldType.INT)
                    .setDefaultValueExpression("0"))
                .addField(new BooleanFieldDef("BOOLEAN1", FieldType.BOOLEAN)
                    .setDefaultValueExpression("false"))
                .addField(new TextFieldDef("STRING1", FieldType.STRING)
                    .setDefaultValueExpression("'DEFAULT STRING VALUE'"))
                .addField(new NumericFieldDef("BYTE1", FieldType.BYTE)
                    .setDefaultValueExpression("88"))
                .addField(new TextFieldDef("CHAR1", FieldType.CHAR)
                    .setDefaultValueExpression("'z'"))
                .addField(new TemporalFieldDef("DATE1", FieldType.DATE)
                    .setDefaultValueExpression("toDate('1999-08-21T15:31:50.000Z')"))
                .addField(new TemporalFieldDef("DATETIME1", FieldType.DATETIME)
                    .setDefaultValueExpression("toDatetime('1999-08-21T15:31:50.000Z')"))
                .addField(new TemporalFieldDef("TIME1", FieldType.TIME)
                    .setDefaultValueExpression("toDatetime('1999-08-21T15:31:50.000Z')"))
                .addField(new NumericFieldDef("INT1", FieldType.INT)
                    .setDefaultValueExpression("99"))
                .addField(new NumericFieldDef("LONG1", FieldType.LONG)
                    .setDefaultValueExpression("1152921504606846976"))
                .addField(new NumericFieldDef("DOUBLE1", FieldType.DOUBLE)
                    .setDefaultValueExpression("10.0"))
                .addField(new NumericFieldDef("FLOAT1", FieldType.FLOAT)
                    .setDefaultValueExpression("99"))
                .addField(new NumericFieldDef("SHORT1", FieldType.SHORT)
                    .setDefaultValueExpression("99"))
                .addField(new NumericFieldDef("BIGDECIMAL1", FieldType.BIG_DECIMAL)
                    .setDefaultValueExpression("'111111111111111111111111111111111111111.111111111111111111111111111111111111111'"))
                .addField(new NumericFieldDef("BIGINTEGER1", FieldType.BIG_INTEGER)
                    .setDefaultValueExpression("'99999999999999999999999999999999999999'"))
            );


        EntityDef entity = schema.getEntity("Entity");
        reader = new TransformingReader(reader)
                .add(new SchemaTransformer(entity));

//        DataWriter writer = new NullWriter();
        DataWriter writer = StreamWriter.newSystemOutWriter();
        
        Job job = Job.run(reader, writer);
        System.out.println("Records Transferred: " + job.getRecordsTransferred());
        System.out.println("Running Time: " + job.getRunningTimeAsString());
    }
}
