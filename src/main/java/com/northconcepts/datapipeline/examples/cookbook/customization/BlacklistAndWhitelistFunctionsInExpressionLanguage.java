package com.northconcepts.datapipeline.examples.cookbook.customization;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Functions;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class BlacklistAndWhitelistFunctionsInExpressionLanguage {

    public static void main(String[] args) {
        callingBlacklistedFunctions();
        whitelistingBlacklistedFunctions();
        callingUnregisteredFunctions();
        whitelistingCustomFunctions();
        blacklistingCustomFunctions();
    }

    private static void callingBlacklistedFunctions() {
        System.out.println("================================Calling Blacklisted Functions================================--");
        try {
            DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                    .setFieldNamesInFirstRow(true);

            TransformingReader transformingReader = new TransformingReader(reader);

            // java.lang.System is blacklisted by default, so this throws instead of exiting the program
            transformingReader.add(new SetCalculatedField("exitProgram", "java.lang.System.exit(0)"));

            Job.run(transformingReader, new StreamWriter(System.out));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void whitelistingBlacklistedFunctions() {
        System.out.println("\n\n================================Whitelist Blacklisted Functions================================--");
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        // a whitelist prefix longer than the blacklisted "java.lang.System" allows just this one method
        Functions.addWhitelistPrefix("java.lang.System.currentTimeMillis");

        TransformingReader transformingReader = new TransformingReader(reader);
        transformingReader.add(new SetCalculatedField("currentTime", "java.lang.System.currentTimeMillis()"));

        Job.run(transformingReader, new StreamWriter(System.out));
    }

    private static void callingUnregisteredFunctions() {
        System.out.println("\n\n================================Calling Unregistered Functions================================--");

        try {
            DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                    .setFieldNamesInFirstRow(true);

            TransformingReader transformingReader = new TransformingReader(reader);
            // public static methods are only callable once registered with Functions.add() or whitelisted, so this throws
            transformingReader.add(new SetCalculatedField("currentTime", "com.northconcepts.datapipeline.examples.cookbook.customization.BlacklistAndWhitelistFunctionsInExpressionLanguage.getCurrentTime()"));

            Job.run(transformingReader, new StreamWriter(System.out));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void whitelistingCustomFunctions() {
        System.out.println("\n\n================================Whitelist Custom Functions================================--");
        DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                .setFieldNamesInFirstRow(true);

        Functions.addWhitelistPrefix("com.northconcepts.datapipeline.examples.cookbook.customization.BlacklistAndWhitelistFunctionsInExpressionLanguage.");

        TransformingReader transformingReader = new TransformingReader(reader);
        transformingReader.add(new SetCalculatedField("currentTime", "com.northconcepts.datapipeline.examples.cookbook.customization.BlacklistAndWhitelistFunctionsInExpressionLanguage.getCurrentTime()"));

        Job.run(transformingReader, new StreamWriter(System.out));
    }

    private static void blacklistingCustomFunctions() {
        System.out.println("\n\n================================Blacklisting Custom Functions================================--");

        try {
            DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                    .setFieldNamesInFirstRow(true);

            // a blacklist prefix longer than the whitelisted class prefix blocks this method again
            Functions.addBlacklistPrefix("com.northconcepts.datapipeline.examples.cookbook.customization.BlacklistAndWhitelistFunctionsInExpressionLanguage.getCurrentTime");

            TransformingReader transformingReader = new TransformingReader(reader);
            transformingReader.add(new SetCalculatedField("currentTime", "com.northconcepts.datapipeline.examples.cookbook.customization.BlacklistAndWhitelistFunctionsInExpressionLanguage.getCurrentTime()"));

            Job.run(transformingReader, new StreamWriter(System.out));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
