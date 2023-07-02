package com.northconcepts.datapipeline.examples.cookbook;

import java.io.FileWriter;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.json.JsonWriter;
import com.northconcepts.datapipeline.json.builder.JsonDetailMarker;
import com.northconcepts.datapipeline.json.builder.JsonObject;
import com.northconcepts.datapipeline.json.builder.JsonTemplate;
import com.northconcepts.datapipeline.memory.MemoryReader;

public class WriteToJsonStreamProgrammatically {
    
    
/* Produces the following JSON (line breaks added for clarity)
  
{
  "actors":{
    "north-america":[
      {
        "stage-name":"John Wayne",
        "real-name":"Marion Robert Morrison",
        "gender":"male",
        "city":"Winterset",
        "balance":156.35
      },
      {
        "stage-name":"Spiderman",
        "real-name":"Peter Parker",
        "gender":"male",
        "city":"New York"
      }
    ]
  }
}

*/

    public static void main(String[] args) throws Throwable {

        Record record1 = new Record();
        record1.setField("stageName", "John Wayne");
        record1.setField("realName", "Marion Robert Morrison");
        record1.setField("gender", "male");
        record1.setField("city", "Winterset");
        record1.setField("balance", 156.35);

        Record record2 = new Record();
        record2.getField("stageName", true).setValue("Spiderman");
        record2.getField("realName", true).setValue("Peter Parker");
        record2.getField("gender", true).setValue("male");
        record2.getField("city", true).setValue("New York");
        record2.getField("balance", true).setValue(-0.96);
               
        MemoryReader reader = new MemoryReader(new RecordList(record1, record2));
        
        
        // create the JSON template
        // arguments to field(), objectField(), arrayField(), and when() are expressions
        // literal strings must be quoted 
        JsonTemplate template = new JsonTemplate();
        
        // detail() marks the node where records are added
        JsonDetailMarker detailElement = template.object().object("'actors'").array("'north-america'").detail();
        
        JsonObject actor = detailElement.object();

        actor.field("'stage-name'", "stageName");
        actor.field("'real-name'", "realName");
        actor.field("'gender'", "gender");
        actor.field("'city'", "city");
        actor.when("balance >= 0").field("'balance'", "balance");  // add this field when balance is not negative

        // add the template to the writer
        JsonWriter writer = new JsonWriter(template, new FileWriter("example/data/output/credit-balance-03.json"));
        
        Job.run(reader, writer);
    }

}
