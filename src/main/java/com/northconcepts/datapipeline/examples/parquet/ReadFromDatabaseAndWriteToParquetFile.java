package com.northconcepts.datapipeline.examples.parquet;

public class ReadFromDatabaseAndWriteToParquetFile {

    static String CREATE_TABLE_QUERY = "create table processing_events\r\n" +
            "(\r\n" +
            "    workflow_name    varchar(200) not null,\r\n" +
            "    product          varchar(200) not null,\r\n" +
            "    effective_date   date         not null,\r\n" +
            "    event            varchar(200) not null,\r\n" +
            "    start_time       timestamp,\r\n" +
            "    end_time         timestamp,\r\n" +
            "    running_time     numeric(24, 10),\r\n" +
            "    records_read     bigint,\r\n" +
            "    records_written  bigint,\r\n" +
            "    read_throughput  numeric(24, 10),\r\n" +
            "    write_throughput numeric(24, 10),\r\n" +
            "    file_size        numeric(24, 10),\r\n" +
            "    input_file       varchar(4000),\r\n" +
            "    output_file      varchar(4000),\r\n" +
            "    timestamp        timestamp    not null,\r\n" +
            "    primary key (workflow_name, product, effective_date, event, timestamp)\r\n" +
            ");";

    static String INSERT_RECORD_QUERY = "INSERT INTO `processing_events`\r\n" +
            "(`workflow_name`, `product`, `effective_date`, `event`, `start_time`, `end_time`, `running_time`,\r\n" +
            "`records_read`, `records_written`, `read_throughput`, `write_throughput`, `file_size`, `input_file`,\r\n" +
            "`output_file`, `timestamp`)\r\n" +
            "VALUES\r\n" +
            "(\r\n" +
            "'iso-mic-meh', 'iso-mic-meh', current_date(), 'validation_result_success', current_timestamp(), current_timestamp(), 10.611272,\r\n" +
            "null, null, null, null, null, 'input.csv', null, current_timestamp()\r\n" +
            ");\r\n" +
            "";

    static String INSERT_RECORD_2_QUERY = "INSERT INTO `processing_events`\r\n" +
            "(`workflow_name`, `product`, `effective_date`, `event`, `start_time`, `end_time`, `running_time`,\r\n" +
            "`records_read`, `records_written`, `read_throughput`, `write_throughput`, `file_size`, `input_file`,\r\n" +
            "`output_file`, `timestamp`)\r\n" +
            "VALUES\r\n" +
            "(\r\n" +
            "'create_event', 'dp-parquet', current_date(), 'jdbc to dp-parquet', current_timestamp(), current_timestamp(), 1234,\r\n" +
            "1234567890, 9087654321, 1234.1234, 9876.9876, 6789, 'input.csv', 'output.csv', current_timestamp()\r\n" +
            ");\r\n" +
            "";

    public static void main(String[] args) {
        //        JdbcFacade jdbcFacade = new JdbcFacade(JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:mem:jdbcTableSort;MODE=PostgreSQL", "sa", ""));
        //        jdbcFacade.executeUpdate(CREATE_TABLE_QUERY);
        //        jdbcFacade.executeUpdate(INSERT_RECORD_QUERY);
        //        jdbcFacade.executeUpdate(INSERT_RECORD_2_QUERY);
        //
        //        JdbcReader reader = new JdbcReader(jdbcFacade.getConnection(), "select * from processing_events limit 100").setAutoCloseConnection(true);
        //        ParquetDataWriter writer = new ParquetDataWriter(new File("example/data/output/jdbc-to-parquet.parquet"));
        //        Job.run(reader, writer);

        System.out.println(0b11111111111111111111111111111111);
        System.out.println(0b10000000000000000000000000000001);
        System.out.println(0b11111111111111111111111111111111L);
        System.out.println(0b100000000000000000000000000000000000000001L);

        System.out.println("----------------");

        System.out.println(Integer.MIN_VALUE);
        System.out.println(Integer.MAX_VALUE);

        System.out.println("----------------");

        System.out.println(Long.MIN_VALUE);
        System.out.println(Long.MAX_VALUE);

        System.out.println("----------------");

        int unsignedInt = 0b11111111111111111111111111111111;
        long unsignedIntAsLong = unsignedInt & 0xFFFFFFFFL;

        System.out.println(unsignedInt);
        System.out.println(unsignedIntAsLong);
        System.out.println(getUnsignedInt(unsignedInt));
        System.out.println(4294967295L | 0xFFFFFFFFL);
        System.out.println();
    }

    public static long getUnsignedInt(int x) {
        // return x & (-1L >>> 32);
        return x & 0xFFFFFFFFL;
    }

}
