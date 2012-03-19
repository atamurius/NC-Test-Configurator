package com.netcracker.tc.tests.examples;

import java.util.List;
import java.util.Map;

import com.netcracker.tc.tests.anns.Param;
import com.netcracker.tc.tests.anns.Params;
import com.netcracker.tc.tests.anns.Scenario;
import com.netcracker.tc.tests.anns.Scenarios;
import com.netcracker.tc.types.standard.num.Num;
import com.netcracker.tc.types.standard.string.Str;

@Scenarios("Type examples")
public class Types
{
    public enum TestEnum
    {
        SOME_VALUE, DEFAULT, OTHER_VALUE, VALUE_3, LAST_VALUE;
        
        public String toString() 
        {
            String name = name().substring(1).toLowerCase().replaceAll("_", " ");
            return name().substring(0,1) + name;
        }
    }
    
    public static class Parameters
    {
        @Param(description = "True of false", defValue = "true")
        public boolean bool; 
        
        @Param(description = "Enum values", defValue = "DEFAULT")
        public TestEnum enumeration;
        
        @Param(description = "List of strings, one per row", defValue = "first,second,last")
        public List<String> list;
        
        @Param(description = "Key-value string pairs, DEL to delete, ENTER to insert", 
                defValue = "key1:value1,key2:value2")
        public Map<String,String> map;
        
        @Param(description = "Integral value from 0 to 100", defValue = "42")
        @Num(min = 0, max = 100)
        public int integer;
        
        @Param(description = "Real value from -1 to 1", defValue = "0.12")
        @Num(min = -1, max = 1)
        public double real;
        
        @Param(description = "String in format 31/12/2012", defValue = "31/12/2012")
        @Str(pattern = "\\d{2}/\\d{2}/\\d{4}")
        public String date;
    }
    
    @Scenario("Example scenario")
    public void scenario(@Params Parameters p) { }
}
