package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.internal.jdbc.JdbcFacade;
import com.northconcepts.datapipeline.jdbc.JdbcConnectionFactory;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.jdbc.JdbcValueReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadFromDatabaseUsingOriginalMapping {

    public static void main(String[] args) {
        JdbcConnectionFactory connectionFactory = JdbcConnectionFactory.wrap("org.h2.Driver", "jdbc:h2:mem:ReadFromDatabaseUsingOpinionatedMapping;MODE=MySQL", "sa", "");

        JdbcFacade jdbcFacade = new JdbcFacade(connectionFactory);
        System.out.println("====================================Start: Executing script====================================");
        jdbcFacade.executeFile(new File("example/data/input", "jewellery.sql"));
        System.out.println("====================================End: Executing script====================================");

        JdbcReader reader = new JdbcReader(connectionFactory, "SELECT * FROM jewellery;");
        reader.setValueReader(JdbcValueReader.ORIGINAL);
        Job.run(reader, new StreamWriter(System.out));

    }
}
