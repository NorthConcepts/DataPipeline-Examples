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

public class SetDefaultValuesFoMissingData {

    public static void main(String... args) throws Throwable {
        DataReader reader = new CSVReader(new File("example/data/input/datamapping/csv-with-null-values.csv"))
                .setFieldNamesInFirstRow(true);
        
        SchemaDef schema = new SchemaDef()
            .addEntity(new EntityDef().setName("Entity")
                .addField(new TextFieldDef().setName("ID").setType(FieldType.STRING).setRequired(true))
                .addField(new BooleanFieldDef().setName("BOOLEAN1").setType(FieldType.BOOLEAN)
                    .setDefaultValueExpression("false"))
                .addField(new TextFieldDef().setName("STRING1").setType(FieldType.STRING)
                    .setDefaultValueExpression("'Default String Value'"))
                .addField(new NumericFieldDef().setName("BYTE1").setType(FieldType.BYTE)
                    .setDefaultValueExpression("toByte(88)"))
                .addField(new TextFieldDef().setName("CHAR1").setType(FieldType.CHAR)
                    .setDefaultValueExpression("com.northconcepts.datapipeline.internal.lang.Util.toChar('z')"))
                .addField(new TemporalFieldDef().setName("DATE1").setType(FieldType.DATE)
                    .setDefaultValueExpression("toDate(now())"))
                .addField(new TemporalFieldDef().setName("DATETIME1").setType(FieldType.DATETIME)
                    .setDefaultValueExpression("now()"))
                .addField(new TemporalFieldDef().setName("TIME1").setType(FieldType.TIME)
                    .setDefaultValueExpression("toTime(now())"))
                .addField(new NumericFieldDef().setName("INT1").setType(FieldType.INT)
                    .setDefaultValueExpression("toInt(9)"))
                .addField(new NumericFieldDef().setName("LONG1").setType(FieldType.LONG)
                    .setDefaultValueExpression("currentTimeMillis()"))
                .addField(new NumericFieldDef().setName("DOUBLE1").setType(FieldType.DOUBLE)
                    .setDefaultValueExpression("999.99"))
                .addField(new NumericFieldDef().setName("FLOAT1").setType(FieldType.FLOAT)
                    .setDefaultValueExpression("99"))
                .addField(new NumericFieldDef().setName("SHORT1").setType(FieldType.SHORT)
                    .setDefaultValueExpression("toShort(99)")));


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
