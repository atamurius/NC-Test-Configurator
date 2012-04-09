package com.netcracker.tconf.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netcracker.sova.annotated.anns.Optional;
import com.netcracker.sova.annotated.anns.Output;
import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.annotated.anns.Params;
import com.netcracker.sova.annotated.anns.Scenario;
import com.netcracker.sova.annotated.anns.Scenarios;
import com.netcracker.sova.types.pub.Num;
import com.netcracker.sova.types.pub.Ref;
import com.netcracker.sova.types.pub.Str;

@Scenarios("Type examples")
public class Types
{
    public enum TestEnum {
        SOME_VALUE, DEFAULT, OTHER_VALUE, VALUE_3, LAST_VALUE }
    
    public static class Parameters
    {
        @Param(description = "True or false")
        boolean bool = true; 
        
        @Param(description = "Enum values")
        TestEnum enumeration = TestEnum.DEFAULT;

        @Optional
        @Param
        TestEnum optionalEnum;

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
        
        @Optional
        @Param
        String optionalString;
        
        @Optional
        @Param
        @Ref("undefined")
        String optionalXML_Reference;
    }
    
    public static class Another
    {
        @Param String someXML_string = "<?xml version=\"1.0\"?>";
    }
    
    @Scenario("Example scenario")
    public void scenario(
            @Param("Test") int test, 
            @Params Parameters p, 
            @Param("Other") int other,
            @Params Another p1) 
    { 
        System.out.println("Parameters passed {");
        System.out.println("test = "+ test);
        System.out.println("p = "+ p.date);
        System.out.println("p.integer = "+ p.integer);
        System.out.println("p.optionalString = "+ p.optionalString);
        System.out.println("p.optionalXML_Reference = "+ p.optionalXML_Reference);
        System.out.println("p.real = "+ p.real);
        System.out.println("p.bool = "+ p.bool);
        System.out.println("Pause...");
        try {
            Thread.sleep(2000);
            System.out.println("p.enumeration = "+ p.enumeration);
            System.out.println("p.list = "+ p.list);
            System.out.println("p.map = "+ p.map);
            System.out.println("p.optionalEnum = "+ p.optionalEnum);
            System.out.println("other = "+ other);
            System.out.println("p1.someXML_string = "+ p1.someXML_string);
            System.out.println("}");
        }
        catch (InterruptedException e) {
        }
    }
    
    @Scenario
    @Output("String")
    public String generateRandomString()
    {
        return "" + (Math.random() * 100);
    }
    
    @Scenario
    public void printNumber(@Param("String") @Ref String number)
    {
        System.out.println("Number is "+ number);
    }
}





