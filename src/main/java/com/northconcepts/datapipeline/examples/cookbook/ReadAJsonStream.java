/*
 * Copyright (c) 2006-2021 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonReader;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class ReadAJsonStream {

/*   Consumes the following JSON (line breaks added for clarity).  Yes, it starts with a new line an two slashes.    

// [
   {
      "id":"358464",
      "t":"MSFT",
      "e":"NASDAQ",
      "l":"31.67",
      "l_cur":"31.67",
      "s":"2",
      "ltt":"4:00PM EDT",
      "lt":"Aug 1, 4:00PM EDT",
      "c":"-0.17",
      "cp":"-0.53",
      "ccol":"chr",
      "el":"31.69",
      "el_cur":"31.69",
      "elt":"Aug 1, 7:54PM EDT",
      "ec":"+0.02",
      "ecp":"0.06",
      "eccol":"chg",
      "div":"0.23",
      "yld":"2.90"
   },
   {
      "id":"419344",
      "t":"ORCL",
      "e":"NYSE",
      "l":"32.75",
      "l_cur":"32.75",
      "s":"2",
      "ltt":"4:00PM EDT",
      "lt":"Aug 1, 4:00PM EDT",
      "c":"+0.40",
      "cp":"1.24",
      "ccol":"chg",
      "el":"32.70",
      "el_cur":"32.70",
      "elt":"Aug 1, 7:15PM EDT",
      "ec":"-0.05",
      "ecp":"-0.15",
      "eccol":"chr",
      "div":"",
      "yld":"1.47"
   },
   {
      "id":"4112",
      "t":"ADBE",
      "e":"NASDAQ",
      "l":"47.70",
      "l_cur":"47.70",
      "s":"0",
      "ltt":"4:00PM EDT",
      "lt":"Aug 1, 4:00PM EDT",
      "c":"+0.42",
      "cp":"0.89",
      "ccol":"chg"
   }
]

*/
    
    public static void main(String[] args) throws Throwable {
    	String url = "http://www.google.com/finance/info?client=ig&q=msft,orcl,adbe";

    	BufferedReader input = new BufferedReader(new InputStreamReader(new URL(url).openStream(), "UTF-8"));
    	
    	// remove preceding slashes from stream
    	input.readLine();
    	input.read();
    	input.read();
    	

    	DataReader reader = new JsonReader(input)
    	    .addField("symbol", "//array/object/t")
    	    .addField("exchange", "//array/object/e")
    	    .addField("price", "//array/object/l")
    	    .addField("change", "//array/object/c")
    	    .addRecordBreak("//array/object");
        
        reader = new TransformingReader(reader)
        	.add(new BasicFieldTransformer("price", "change").stringToDouble())
        	;
    	
        DataWriter writer = new  StreamWriter(System.out);
   
        Job.run(reader, writer);
    }

}
