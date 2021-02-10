package com.northconcepts.datapipeline.examples.parquet;

import static org.apache.parquet.schema.LogicalTypeAnnotation.stringType;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.BINARY;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.BOOLEAN;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.DOUBLE;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.INT32;

import java.io.File;

import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Types;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;

public class ReadSelectedFieldsFromAParquetFile {

    public static void main(String[] args) {
        // Prepare schema with specified fields and those fields will be read from file.
        MessageType schema = new MessageType("input_schema",
            Types.optional(INT32).named("id"),
            Types.optional(BOOLEAN).named("bool_col"),
            Types.optional(INT32).named("int_col"),
            Types.optional(DOUBLE).named("double_col"),
            Types.optional(BINARY).as(stringType()).named("date_string_col"));

        ParquetDataReader reader = new ParquetDataReader(new File("example/data/input/read_parquet_file.parquet"));
        reader.setSchema(schema); // provide input schema and read only selected fields specified in schema.
        Job.run(reader, new StreamWriter(System.out));

        System.out.println(reader.getSchema());
    }
}
