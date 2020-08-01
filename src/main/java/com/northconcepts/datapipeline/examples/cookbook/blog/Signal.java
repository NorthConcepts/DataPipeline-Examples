/*
 * Copyright (c) 2006-2020 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook.blog;

public class Signal {

    private String source;
    private String name;
    private String component;
    private int occurrence;
    private int size;
    private float bandwidth;
    private int sizewithHeader;
    private float bandwidthWithHeader;

    public Signal() {
    }

    public Signal(String source, String name, String component, int occurrence, int size, float bandwidth, int sizewithHeader,
            float bandwidthWithHeader) {
        this.source = source;
        this.name = name;
        this.component = component;
        this.occurrence = occurrence;
        this.size = size;
        this.bandwidth = bandwidth;
        this.sizewithHeader = sizewithHeader;
        this.bandwidthWithHeader = bandwidthWithHeader;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(float bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getSizewithHeader() {
        return sizewithHeader;
    }

    public void setSizewithHeader(int sizewithHeader) {
        this.sizewithHeader = sizewithHeader;
    }

    public float getBandwidthWithHeader() {
        return bandwidthWithHeader;
    }

    public void setBandwidthWithHeader(float bandwidthWithHeader) {
        this.bandwidthWithHeader = bandwidthWithHeader;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

}
