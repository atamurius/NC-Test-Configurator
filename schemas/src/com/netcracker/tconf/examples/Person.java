package com.netcracker.tconf.examples;

import com.netcracker.sova.annotated.anns.Default;
import com.netcracker.sova.annotated.anns.Output;
import com.netcracker.sova.annotated.anns.Outputs;
import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.annotated.anns.Params;
import com.netcracker.sova.annotated.anns.Scenario;
import com.netcracker.sova.annotated.anns.Scenarios;
import com.netcracker.sova.types.anns.Num;
import com.netcracker.sova.types.ref.Ref;

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
        System.out.printf("Person.create(firstName = %s, lastName = %s, age = %d, gender = %s)%n",
                firstName, lastName, age, gender);
        return "Some id";
    }
    
    @Scenario
    public void delete(@Param("Person") @Ref("person:id") String personId) 
    { 
        System.out.printf("Person.delete(id = %s)%n", personId);
    }
    
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
        System.out.println("Person.generateRandomPersons()");
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
    public void modify(@Params ModifyParams params) 
    { 
        System.out.printf("Person.modify(firstName = %s, lastName = %s, age = %d, gender = %s)%n",
                params.firstName, params.lastName, params.age, params.gender);
    }
}




