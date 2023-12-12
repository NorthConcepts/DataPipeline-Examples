package com.northconcepts.datapipeline.foundations.examples.jdbc;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.jdbc.sql.select.Select;
import com.northconcepts.datapipeline.job.DataReaderFactory;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.sql.mysql.CreateMySqlDdlFromSchemaDef;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class JoinCsvFiles {

    private static final File FILE1 = new File("example/data/input/user_account.csv");
    private static final File FILE2 = new File("example/data/input/credit-balance-insert-records2.csv");
    
    private static final String TABLE1 = "Account";
    private static final String TABLE2 = "CreditBalance";
    
    public static final String DATABASE_FILE = new File("example/data/output/JoinCSVFiles.h2").getAbsolutePath();

    
    public static void main(String[] args) throws Throwable {
        
        DataReaderFactory dataReaderFactory1 = () -> new CSVReader(FILE1).setFieldNamesInFirstRow(true);
        DataReaderFactory dataReaderFactory2 = () -> new CSVReader(FILE2).setFieldNamesInFirstRow(true);
        
        GenerateEntityFromDataset entityGenerator = new GenerateEntityFromDataset();
        EntityDef entityDef1 = entityGenerator.generateEntity(dataReaderFactory1.createDataReader()).setName(TABLE1);
        EntityDef entityDef2 = entityGenerator.generateEntity(dataReaderFactory2.createDataReader()).setName(TABLE2);
        SchemaDef schemaDef = new SchemaDef().addEntity(entityDef1).addEntity(entityDef2);
        

        JdbcConnectionFactory connectionFactory = JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:file:" + DATABASE_FILE + ";MODE=MySQL", "sa", "");
        
        createTables(schemaDef, connectionFactory);

        Job job1 = importFileToDatabase(dataReaderFactory1, entityDef1, connectionFactory, TABLE1);
        Job job2 = importFileToDatabase(dataReaderFactory2, entityDef2, connectionFactory, TABLE2);

        job1.waitUntilFinished();
        job2.waitUntilFinished();

        Select select = new Select("CreditBalance")
            .leftJoin("Account", "CreditBalance.Account=Account.AccountNo")
            ;

        DataReader reader = new JdbcReader(connectionFactory, select.getSqlFragment());
        DataWriter writer = new CSVWriter(new File("example/data/output/joined-csv.csv"));
        Job.run(reader, writer);
    }

    private static Job importFileToDatabase(DataReaderFactory dataReaderFactory, EntityDef entityDef, JdbcConnectionFactory connectionFactory, String tableName) {
        DataReader reader = dataReaderFactory.createDataReader();
        reader = new TransformingReader(reader).add(new SchemaTransformer(entityDef));
        return Job.runAsync(reader, new JdbcWriter(connectionFactory, tableName));
    }

    private static void createTables(SchemaDef schemaDef, JdbcConnectionFactory jdbcConnectionFactory) throws Throwable {
        CreateMySqlDdlFromSchemaDef ddl = new CreateMySqlDdlFromSchemaDef(schemaDef)
            .setPretty(true)
            .setDropTable(true)
            .setCheckIfTableNotExists(false)
            ;

        String sql = ddl.getSqlFragment();
        
        System.out.println(sql);
        System.out.println("--------------------------");
        
        try (Connection connection = jdbcConnectionFactory.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.execute();
            }
        }
    }

}
