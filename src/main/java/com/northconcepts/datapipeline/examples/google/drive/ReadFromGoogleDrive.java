package com.northconcepts.datapipeline.examples.google.drive;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.googledrive.GoogleDriveFileSystem;
import com.northconcepts.datapipeline.job.Job;

public class ReadFromGoogleDrive {

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
            List<com.google.api.services.drive.model.File> files = fileSystem.listRootFolder();
            if (files == null || files.size() == 0) {
                System.out.println("No files found.");
            } else {
                System.out.println("Files:");
                for (com.google.api.services.drive.model.File file : files) {
                    System.out.printf("%s (%s)%n", file.getName(), file.getId());
                }
            }

            // "cached-lookup.csv" must already exist in the root of your Google Drive.
            InputStream inputStream = fileSystem.readFile("cached-lookup.csv");

            DataReader reader = new CSVReader(new InputStreamReader(inputStream))
                    .setFieldNamesInFirstRow(true);
            DataWriter writer = StreamWriter.newSystemOutWriter();
            Job.run(reader, writer);

            System.out.println("Records read: " + writer.getRecordCount());
        } finally {
            fileSystem.close();
        }
    }

}
