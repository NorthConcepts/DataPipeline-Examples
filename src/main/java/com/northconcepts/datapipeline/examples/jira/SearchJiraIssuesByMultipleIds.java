/*
 * Copyright (c) 2006-2023 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.jira;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.jira.JiraIssueReader;
import com.northconcepts.datapipeline.jira.JiraSearch;
import com.northconcepts.datapipeline.job.Job;

public class SearchJiraIssuesByMultipleIds {

    private static final String JIRA_DOMAIN = "JIRA_DOMAIN";
    private static final String JIRA_USERNAME = "USERNAME";
    private static final String JIRA_API_KEY = "API_KEY";
    
    public static void main(String[] args) {
        JiraSearch jiraSearch = new JiraSearch();
        jiraSearch.searchIssuesById("AB-1", "AB-2", "AB-6"); // your JIRA ids

        JiraIssueReader reader = new JiraIssueReader(JIRA_DOMAIN, JIRA_USERNAME, JIRA_API_KEY, jiraSearch);
        Job.run(reader, new StreamWriter(System.out));
    }
}
