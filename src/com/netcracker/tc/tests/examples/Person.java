package com.netcracker.tc.tests.examples;

import com.netcracker.tc.tests.anns.Action;
import com.netcracker.tc.tests.anns.Output;
import com.netcracker.tc.tests.anns.Parameter;
import com.netcracker.tc.tests.anns.Scenario;
import com.netcracker.tc.tests.anns.Scenarios;
import com.netcracker.tc.types.standard.anns.Int;
import com.netcracker.tc.types.standard.anns.Ref;

@Scenarios
public class Person
{
    public static enum Gender { MALE, FEMALE };

    @Scenario
    public static class Create
    {
        @Parameter
        public String firstName;

        @Parameter
        public String lastName;
        
        @Parameter
        @Int(min = 1)
        public int age;
        
        @Parameter
        public Gender gender = Gender.MALE;
        
        @Output(type = "person:id")
        public String id;
        
        @Action
        public void execute()
        {
            id = "some result id";
        }
    }
    
    @Scenario
    public static class Delete
    {
        @Parameter
        @Ref("person:id")
        public String personId;
        
        @Action
        public void execute() { }
    }
}




