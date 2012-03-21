package com.netcracker.tc.tests.examples;

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

    @Scenario(
        output = @Output(value = "Id", type = "person:id")
    )
    public String create(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("age") @Num(min = 1) int age,
            @Param("gender") Gender gender) {
        
        return "Some id";
    }
    
    @Scenario
    public void delete(@Param("person") @Ref("person:id") String personId) {
    }
    
    @Outputs
    public static class RandomPersons
    {
        @Output(type = "person:id")
        public String firstPerson = "random1";
        
        @Output(type = "person:id")
        public String secondPerson = "random2";
    }
    
    @Scenario
    public RandomPersons generateRandomPersons()
    {
        return new RandomPersons();
    }
    
    public static class ModifyParams
    {
        @Param
        public String firstName;
        
        @Param
        public String lastName;
        
        @Param 
        @Num(min = 1) 
        public int age;

        @Param
        public Gender gender;
    }
    
    @Scenario
    public void modify(@Params ModifyParams params)
    {
        // Nothing to do here
    }
}




