/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.jira;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.jira.JiraEpicReader;
import com.northconcepts.datapipeline.job.Job;

public class ReadEpicsFromAJiraBoard {

    private static final String JIRA_DOMAIN = "JIRA_DOMAIN";
    private static final String JIRA_USERNAME = "USERNAME";
    private static final String JIRA_API_KEY = "API_KEY";
    private static final int JIRA_BOARD_ID = 4;

    public static void main(String... args) {
        JiraEpicReader reader = new JiraEpicReader(JIRA_DOMAIN, JIRA_USERNAME, JIRA_API_KEY, JIRA_BOARD_ID);
        reader.setMaxItems(5);

        Job.run(reader, new StreamWriter(System.out));
    }
}
