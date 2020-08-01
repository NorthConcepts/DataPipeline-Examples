/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.jdbc.JdbcMultiWriter;
import com.northconcepts.datapipeline.job.Job;

public class WriteToDatabaseUsingMultipleConnections {

    public static void main(String[] args) {
        
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("datapipeline");
        dataSource.setUser("etl");
        dataSource.setPassword("etl");
        
        ExcelDocument doc = new ExcelDocument();
        DataReader reader = new ExcelReader(doc.open(
                new File("example/data/input/call-center-inbound-call-2.xlsx")))
                .setFieldNamesInFirstRow(true);

        DataWriter writer = new JdbcMultiWriter(dataSource, 5, 10, "inbound_calls");

        Job.run(reader, writer);
    }
/* JdbcMultiWriter does not create the table and columns, it must be created beforehand.
When setFieldNamesInFirstRow(true), the column names must equal the field names.

mysql> describe inbound_calls;
+--------------+-------------+------+-----+---------+-------+
| Field        | Type        | Null | Key | Default | Extra |
+--------------+-------------+------+-----+---------+-------+
| event_type   | varchar(45) | YES  |     | NULL    |       |
| id           | varchar(45) | YES  |     | NULL    |       |
| agent_id     | varchar(45) | YES  |     | NULL    |       |
| phone_number | varchar(45) | YES  |     | NULL    |       |
| start_time   | varchar(45) | YES  |     | NULL    |       |
| end_time     | varchar(45) | YES  |     | NULL    |       |
| disposition  | varchar(45) | YES  |     | NULL    |       |
+--------------+-------------+------+-----+---------+-------+

When setFieldNamesInFirstRow(false), the column names start at A, B... so on (Excel style)

mysql> describe inbound_calls;
+-------+-------------+------+-----+---------+-------+
| Field | Type        | Null | Key | Default | Extra |
+-------+-------------+------+-----+---------+-------+
| A     | varchar(45) | YES  |     | NULL    |       |
| B     | varchar(45) | YES  |     | NULL    |       |
| C     | varchar(45) | YES  |     | NULL    |       |
| D     | varchar(45) | YES  |     | NULL    |       |
| E     | varchar(45) | YES  |     | NULL    |       |
| F     | varchar(45) | YES  |     | NULL    |       |
| G     | varchar(45) | YES  |     | NULL    |       |
+-------+-------------+------+-----+---------+-------+
7 rows in set (0.00 sec)

*/
}
