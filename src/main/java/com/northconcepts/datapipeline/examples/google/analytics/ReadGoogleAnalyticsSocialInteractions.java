package com.northconcepts.datapipeline.examples.google.analytics;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.google.analytics.GoogleAnalyticsReportReader;
import com.northconcepts.datapipeline.job.Job;


/**
 *
 * CLIENT_SECRET is your Service account credentials created in the Google Developer Console
 * and stored in the resources folder.
 */
public class ReadGoogleAnalyticsSocialInteractions {

    private static final String CLIENT_SECRET = "/My Project-5ed7b6c44f35.json";
    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String VIEW_ID = "80548296";
    /** Authorizes the installed application to access user's protected data. */
    private static GoogleCredential authorize(NetHttpTransport httpTransport) throws Throwable {
        return GoogleCredential.fromStream(
                ReadGoogleAnalyticsSocialInteractions.class.getResourceAsStream(CLIENT_SECRET), httpTransport, JSON_FACTORY)
                .createScoped(AnalyticsReportingScopes.all());
    }

    public static void main(String[] args) throws Throwable {

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        DataReader reader = new GoogleAnalyticsReportReader(authorize(httpTransport), VIEW_ID)
                .addDateRange("14DaysAgo", "today")
                .addDimension("ga:socialInteractionNetwork")
                .addDimension("ga:socialInteractionAction")
                .addDimension("ga:socialInteractionNetworkAction")
                .addDimension("ga:socialInteractionTarget")
                .addDimension("ga:socialEngagementType")
                .addMetric("ga:socialInteractions")
                .addMetric("ga:uniqueSocialInteractions")
                .addMetric("ga:socialInteractionsPerSession")
                .setApplicationName(APPLICATION_NAME);
        DataWriter writer = StreamWriter.newSystemOutWriter();

        Job.run(reader, writer);

    }
}
