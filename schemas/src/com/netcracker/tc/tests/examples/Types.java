package com.netcracker.tc.tests.examples;

import java.util.Arrays;
import java.util.HashMap;
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
    public enum TestEnum {
        SOME_VALUE, DEFAULT, OTHER_VALUE, VALUE_3, LAST_VALUE }
    
    public static class Parameters
    {
        @Param(description = "True of false")
        boolean bool = true; 
        
        @Param(description = "Enum values")
        TestEnum enumeration = TestEnum.DEFAULT;
        
        @Param(description = "List of strings, one per row")
        List<String> list = Arrays.asList("first","second","third");
        
        @Param(description = "Key-value string pairs, DEL to delete, ENTER to insert")
        Map<String,String> map = new HashMap<String, String>();
        {
            map.put("key1", "value1");
            map.put("key2", "value2");
            map.put("key3", "value3");
        }
        
        @Param(description = "Integral value from 0 to 100")
        @Num(min = 0, max = 100)
        int integer = 42;
        
        @Param(description = "Real value from -1 to 1")
        @Num(min = -1, max = 1)
        double real;
        
        @Param(description = "String in format 31/12/2012")
        @Str(pattern = "\\d{2}/\\d{2}/\\d{4}")
        String date = "12/03/2012";
    }
    
    static class Another
    {
        @Param String someXML_string = "<?xml version=\"1.0\"?>";
    }
    
    @Scenario("Example scenario")
    public void scenario(
            @Param("Test") int test, 
            @Params Parameters p, 
            @Param("Other") int other,
            @Params Another p1) { }
}
