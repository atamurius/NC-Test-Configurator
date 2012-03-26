package com.netcracker.tc.tests.examples;

import com.netcracker.tc.tests.anns.Default;
import com.netcracker.tc.tests.anns.Output;
import com.netcracker.tc.tests.anns.Outputs;
import com.netcracker.tc.tests.anns.Param;
import com.netcracker.tc.tests.anns.Params;
import com.netcracker.tc.tests.anns.Scenario;
import com.netcracker.tc.tests.anns.Scenarios;
import com.netcracker.tc.types.standard.num.Num;
import com.netcracker.tc.types.standard.ref.Ref;

/**
 * Schema example.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
@Scenarios
public class Person
{
    public static enum Gender { MALE, FEMALE };

    @Scenario
    @Output(value = "ID", type = "person:id")
    public String create(
            @Param("First name") 
            String firstName,
            @Param("Last name") 
            String lastName,
            @Param("Age") @Num(min = 1) @Default("18") 
            int age,
            @Param("Gender") 
            Gender gender
    ) {
        return "Some id";
    }
    
    @Scenario
    public void delete(@Param("Person") @Ref("person:id") String personId) { }
    
    @Outputs
    public static class RandomPersons
    {
        @Output(type = "person:id")
        String firstPerson = "random1";
        
        @Output(type = "person:id")
        String secondPerson = "random2";
    }
    
    @Scenario
    public RandomPersons generateRandomPersons()
    {
        return new RandomPersons();
    }
    
    public static class ModifyParams
    {
        @Param
        String firstName = "Jhon";
        
        @Param
        String lastName = "Doe";
        
        @Param 
        @Num(min = 1) 
        int age = 18;

        @Param
        Gender gender = Gender.MALE;
    }
    
    @Scenario
    public void modify(@Params ModifyParams params) { }
}




