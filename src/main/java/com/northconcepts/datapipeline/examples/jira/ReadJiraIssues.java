/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.jira;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.jira.JiraIssueReader;
import com.northconcepts.datapipeline.jira.JiraSearch;
import com.northconcepts.datapipeline.job.Job;

public class ReadJiraIssues {

    private static final String JIRA_DOMAIN = "JIRA_DOMAIN";
    private static final String JIRA_USERNAME = "USERNAME";
    private static final String JIRA_API_KEY = "API_KEY";
    private static final String JQL = "YOUR_JIRA_QUERY";

    public static void main(String... args) {
        JiraSearch jiraSearch = new JiraSearch()
            .setJql(JQL)
            .setMaxResults(5);

        JiraIssueReader reader = new JiraIssueReader(JIRA_DOMAIN, JIRA_USERNAME, JIRA_API_KEY, jiraSearch);
        Job.run(reader, new StreamWriter(System.out));
    }
}
