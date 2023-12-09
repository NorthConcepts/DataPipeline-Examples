package com.northconcepts.datapipeline.foundations.examples.jdbc;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.tools.GenerateEntityFromDataset;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcWriter;
import com.northconcepts.datapipeline.jdbc.sql.select.Select;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.sql.mysql.CreateMySqlDdlFromSchemaDef;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class JoinCsvFiles {

    public static final String databaseFilePath = new File("example/data/output/JoinCSVFiles.h2").getAbsolutePath();

    public static void main(String[] args) throws Throwable {

        JdbcConnectionFactory connectionFactory = JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:file:" + databaseFilePath + ";MODE=MySQL", "sa", "");
        createTables(connectionFactory);

        DataWriter writer = new JdbcWriter(connectionFactory, "CreditBalance");
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-insert-records2.csv")).setFieldNamesInFirstRow(true);
        Job job1 = Job.runAsync(reader, writer);

        writer = new JdbcWriter(connectionFactory, "Account");
        reader = new CSVReader(new File("example/data/input/user_account.csv")).setFieldNamesInFirstRow(true);
        reader = new TransformingReader(reader).add(new BasicFieldTransformer("DoB").stringToDate("yyyy-MM-dd"));
        Job job2 = Job.runAsync(reader, writer);

        job1.waitUntilFinished();
        job2.waitUntilFinished();

        Select select = new Select("CreditBalance")
            .select("CreditBalance.*", "Account.*")
            .leftJoin("Account", "CreditBalance.Account=Account.AccountNo")
            ;

        reader = new JdbcReader(connectionFactory, select.getSqlFragment());
        Job.run(reader, new CSVWriter(new File("example/data/output/joined-csv.csv")));
    }

    public static void createTables(JdbcConnectionFactory jdbcConnectionFactory) throws Throwable {
        GenerateEntityFromDataset generator = new GenerateEntityFromDataset();
        SchemaDef schemaDef = new SchemaDef();

        EntityDef entityDef = generator.generateEntity(
            new CSVReader(new File("example/data/input/credit-balance-insert-records2.csv")).setFieldNamesInFirstRow(true)
        ).setName("CreditBalance");
        schemaDef.addEntity(entityDef);

        entityDef = generator.generateEntity(
            new CSVReader(new File("example/data/input/user_account.csv")).setFieldNamesInFirstRow(true)
        ).setName("Account");
        schemaDef.addEntity(entityDef);

        CreateMySqlDdlFromSchemaDef ddl = new CreateMySqlDdlFromSchemaDef(schemaDef)
            .setPretty(true)
            .setDropTable(true)
            .setCheckIfTableNotExists(false)
            ;

        try (Connection connection = jdbcConnectionFactory.createConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ddl.getSqlFragment())) {
                preparedStatement.execute();
            }
        }

    }
}
