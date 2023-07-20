package com.northconcepts.datapipeline.foundations.examples.schema;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.foundations.schema.BooleanFieldDef;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class TransformRecordsUsingSchema {

    public static void main(String[] args) {
        EntityDef entityDef = new EntityDef();
        entityDef.addField(new TextFieldDef("name", FieldType.STRING).setRequired(true).setAllowBlank(false).setMaximumLength(100));
        entityDef.addField(new NumericFieldDef("age", FieldType.INT).setRequired(true).setMinimum(25).setMaximum(75));
        entityDef.addField(new NumericFieldDef("balance", FieldType.BIG_DECIMAL));
        entityDef.addField(new BooleanFieldDef("active", FieldType.BOOLEAN).setAllowedValues(null));
        entityDef.addField(new TemporalFieldDef("lastUpdated", FieldType.DATE).setPattern("yyyy-MM-dd"));
        
        Record record1 = new Record();
        record1.addField("name", "John Smith");
        record1.addField("age", "72");
        record1.addField("balance", "31.05");
        record1.addField("active", "true"); // "yes" and non-zero numbers also map to true 
        record1.addField("lastUpdated", "2019-12-19");

        Record record2 = new Record();
        record2.addField("name", "Jane Powers");
        record2.addField("age", "26");
        record2.addField("balance", null);
        record2.addField("active", "false"); // "yes" and non-zero numbers also map to true 
        record2.addField("lastUpdated", "2020-10-30");

        DataReader reader = new MemoryReader(new RecordList(record1, record2));
        reader = new TransformingReader(reader).add(new SchemaTransformer(entityDef));
        DataWriter writer = StreamWriter.newSystemOutWriter();
        Job.run(reader, writer);
    }

}
