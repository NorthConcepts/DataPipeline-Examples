/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import com.northconcepts.datapipeline.diagnostic.Diagnostic;

public class LogDiagnosticInfo {

    public static void main(String[] args) {
        // Approach 1:
        new Diagnostic().log();

        System.out.println("\n\n\n");

        // Approach 2:
        System.out.println(new Diagnostic().toString());
    }
}
