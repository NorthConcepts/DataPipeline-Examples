package com.northconcepts.datapipeline.examples.google.calendar;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.LimitReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.google.calendar.GoogleCalendarReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadGoogleCalendar {

    private static final int MAX_RESULTS = 10;
    private static final String APPLICATION_NAME = "GoogleCalendarScratch";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/calendar";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CLIENT_SECRET = "/integrations.json";

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize(NetHttpTransport httpTransport) throws Throwable {

        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(ReadGoogleCalendar.class.getResourceAsStream(CLIENT_SECRET)));
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        // authorize
        LocalServerReceiver receier = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receier).authorize("user");
    }

    public static void main(String[] args) throws Throwable {

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        DataReader reader = new GoogleCalendarReader(authorize(httpTransport), httpTransport)
                    .setBatchSize(MAX_RESULTS)
                    .setTimeMin(new Date())
                    .setTimeMax(null)
                    .setUpdatedMin(null)
                    .setOrderBy("startTime")
                    .setSingleEvent(true)
                    .setAlwaysIncludeEmail(true)
                    .setApplicationName(APPLICATION_NAME);

        reader = new LimitReader(reader, MAX_RESULTS);

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }
}
