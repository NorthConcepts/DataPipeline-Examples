package com.northconcepts.datapipeline.examples.google.drive;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.NullWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelReader;
import com.northconcepts.datapipeline.googledrive.GoogleDriveFileSystem;
import com.northconcepts.datapipeline.job.Job;

public class ReadFromAGoogleDriveFolder {

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
            // "First SubFolder" must already exist in your Google Drive.
            List<com.google.api.services.drive.model.File> files = fileSystem.listFolder("First SubFolder");
            if (files == null || files.size() == 0) {
                System.out.println("No files found.");
            } else {
                System.out.println("Files in First SubFolder:");
                for (com.google.api.services.drive.model.File file : files) {
                    System.out.printf("%s (%s)%n", file.getName(), file.getId());
                }
            }

            // "call-center-inbound-call-2.xls" must already exist inside "First SubFolder".
            ExcelDocument document = new ExcelDocument()
                    .open(fileSystem.readFile("First SubFolder", "call-center-inbound-call-2.xls"));
            DataReader reader = new ExcelReader(document)
                    .setSheetName("balance")
                    .setFieldNamesInFirstRow(true);
            DataWriter writer = new NullWriter();
            Job.run(reader, writer);

            System.out.println("Records read: " + writer.getRecordCount());
        } finally {
            fileSystem.close();
        }
    }

}
