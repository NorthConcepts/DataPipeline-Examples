/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
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
        // Prepare schema with fields to be read from file, ignoring other fields
        MessageType schema = new MessageType("input_schema",
                Types.optional(INT32).named("id"),
                Types.optional(INT32).named("int_col"),
                Types.optional(DOUBLE).named("double_col"),
                Types.optional(BINARY).as(stringType()).named("date_string_col"),
                Types.optional(BOOLEAN).named("bool_col")
                );
/* 
       Exclude the following fields from being read:
            optional int32 tinyint_col;
            optional int32 smallint_col;
            optional int64 bigint_col;
            optional float float_col;
            optional binary string_col;
            optional int96 timestamp_col;
*/
        
        ParquetDataReader reader = new ParquetDataReader(new File("example/data/input/read_parquet_file.parquet"))
                .setSchema(schema)  // Remove this line to see all fields (and in original arrangement)
                ;
        Job.run(reader, new StreamWriter(System.out));

        System.out.println(reader.getSchema());
    }
}
