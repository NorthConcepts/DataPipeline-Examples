package com.northconcepts.datapipeline.foundations.examples.number;

import com.northconcepts.datapipeline.foundations.number.NumberDescriptor;
import com.northconcepts.datapipeline.foundations.number.NumberDetector;
import com.northconcepts.datapipeline.foundations.number.NumberMatch;

public class DetectNumbersInStrings {
    
    public static void main(String[] args) {

        detectNumber("23");
        detectNumber("-236");
        detectNumber("23.49");
        detectNumber("23L");
        detectNumber("23_3");
      
    }
    
    public static void detectNumber(String value){
        NumberDetector detector = new NumberDetector();

        NumberMatch numberMatch = detector.match(value);

        System.out.println("=======================================================================");
        System.out.println("String: "+value);

        if (numberMatch == null) {
            System.out.println("    ------ NO MATCHING NUMBER ------");
            return;
        }

        System.out.println("    Source:        " + numberMatch.getSource());
        System.out.println("    WholePart:     " + numberMatch.getWholePart());
        System.out.println("    FractionPart:  " + numberMatch.getFractionPart());
        System.out.println("    Number:        " + numberMatch.getNumber());

        NumberDescriptor numberDescriptor = numberMatch.getNumberDescriptor();
        if (numberDescriptor != null) {
            System.out.println("    NumberDescriptor:");
            System.out.println("        FieldType:      " + numberDescriptor.getFieldType());
            System.out.println("        WholeDigits:    " + numberDescriptor.getWholeDigits());
            System.out.println("        Precision:      " + numberDescriptor.getPrecision());
            System.out.println("        Scale:          " + numberDescriptor.getScale());
            System.out.println("        FractionDigits: " + numberDescriptor.getFractionDigits());
            System.out.println("        IsNegative:     " + numberDescriptor.isNegative());
            System.out.println("        IsSigned:       " + numberDescriptor.isSigned());
        }
    }
    
}
