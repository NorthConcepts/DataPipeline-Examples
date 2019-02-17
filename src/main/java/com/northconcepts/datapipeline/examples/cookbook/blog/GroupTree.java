package com.northconcepts.datapipeline.examples.cookbook.blog;

import com.northconcepts.datapipeline.core.ArrayValue;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.group.GroupOperation;
import com.northconcepts.datapipeline.group.GroupOperationField;
import com.northconcepts.datapipeline.internal.lang.Util;


public class GroupTree extends GroupOperation<ArrayValue> {
    
    public GroupTree(String targetFieldName) {
        super(null, targetFieldName, false);
    }

    @Override
    protected GroupOperationField<ArrayValue> createField() {
        return new GroupOperationField<ArrayValue>(this) {
            
            private Record rootRecord = new Record();

            @Override
            protected void apply(Record record, Field field) {
                Record parentDataRecord = rootRecord;
                
                FIELD_LOOP:
                for (int i = 1; i < record.getFieldCount(); i++) {
                    Field sourceField = record.getField(i);

                    ArrayValue dataArray = parentDataRecord.getField(getTargetFieldName(), true).getValueAsArray(true);
                    if (dataArray.size() > 0) {
                        Record lastDataRecord = dataArray.getValueAsRecord(dataArray.size() - 1);
                        Field lastDataRecordField = lastDataRecord.getField(sourceField.getName(), true);
                        boolean sameTreeBranch = Util.equals(lastDataRecordField.getValue(), sourceField.getValue());
                        if (sameTreeBranch) {
                            parentDataRecord = lastDataRecord;
                            continue FIELD_LOOP; 
                        }
                    }
                    
                    Record lastDataRecord = new Record();
                    lastDataRecord.setField(sourceField.getName(), sourceField.getValue());
                    dataArray.addValue(lastDataRecord);
                    parentDataRecord = lastDataRecord;
                }
            }
            
            @Override
            protected ArrayValue getValue() {
                return rootRecord.getField(0).getValueAsArray();
            }
            
        };
    }
    
}
