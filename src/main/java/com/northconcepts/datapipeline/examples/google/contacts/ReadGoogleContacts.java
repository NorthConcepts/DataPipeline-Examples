/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.google.contacts;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.LimitReader;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.google.contacts.GoogleContactsReader;
import com.northconcepts.datapipeline.job.Job;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class ReadGoogleContacts {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/contacts";
    private static final List<String> SCOPES = Collections.singletonList(PeopleServiceScopes.CONTACTS_READONLY);
    private static final String CLIENT_SECRET = "/integrations.json";
    private static final String APPLICATION_NAME = "GoogleContactsScratch";
    private static final int MAX_RESULTS = 10;
    private static final String PERSON_FIELDS = "names,emailAddresses,phoneNumbers";
    private static final String RESOURCE = "people/me";


    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize(NetHttpTransport httpTransport) throws Throwable {

        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(ReadGoogleContacts.class.getResourceAsStream(CLIENT_SECRET)));
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        // authorize
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String[] args) throws Throwable {

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        DataReader reader = new GoogleContactsReader(authorize(httpTransport), httpTransport)
                .setPersonFields(PERSON_FIELDS)
                .setBatchSize(MAX_RESULTS)
                .setApplicationName(APPLICATION_NAME);
        reader = new LimitReader(reader, MAX_RESULTS);

        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);
    }
}
