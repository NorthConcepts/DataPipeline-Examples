package com.northconcepts.datapipeline.examples.google.drive;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.googledrive.GoogleDriveFileSystem;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.SimpleJsonWriter;

public class WriteToGoogleDrive {

    private static final String APPLICATION_NAME = "datapipeline";
    private static final String USER_NAME = "YOUR GOOGLE DRIVE USERNAME";
    private static final File CLIENT_SECRETS = new File("example/data/input/client_id.json");
    private static final File CREDENTIALS_DIR = new File("example", "credentials");

    // On first run you will be prompted by Google to authorize access in a browser.
    // Once a credential has been stored in CREDENTIALS_DIR, Google will not prompt again
    // unless the stored credential is deleted.
    public static void main(String[] args) throws Throwable {
        GoogleDriveFileSystem fileSystem = new GoogleDriveFileSystem(APPLICATION_NAME, USER_NAME,
                new FileInputStream(CLIENT_SECRETS), CREDENTIALS_DIR);
        fileSystem.open();

        try {
            DataReader reader = new CSVReader(new File("example/data/input/credit-balance-01.csv"))
                    .setFieldNamesInFirstRow(true);

            // Writes "credit-balance-01.json" to the root of your Google Drive.
            OutputStreamWriter osw = new OutputStreamWriter(fileSystem.writeFile("credit-balance-01.json"));
            DataWriter writer = new SimpleJsonWriter(osw)
                    .setPretty(true);

            Job.run(reader, writer);

            System.out.println("Records written: " + writer.getRecordCount());
        } finally {
            fileSystem.close();
        }
    }

}
