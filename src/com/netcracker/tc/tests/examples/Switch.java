package com.netcracker.tc.tests.examples;

import java.util.HashMap;
import java.util.Map;

import com.netcracker.tc.tests.anns.Action;
import com.netcracker.tc.tests.anns.Output;
import com.netcracker.tc.tests.anns.Parameter;
import com.netcracker.tc.tests.anns.Scenario;
import com.netcracker.tc.tests.anns.Scenarios;
import com.netcracker.tc.types.standard.anns.Int;
import com.netcracker.tc.types.standard.anns.Ref;

@Scenarios
public class Switch
{
    public String id;
    public Map<Integer,String> portIds = new HashMap<Integer, String>();
    
    @Scenario
    public static class Create
    {
        @Parameter
        public String name;
        
        @Parameter
        @Int(min = 1)
        public int portCount;
        
        @Output("switch")
        public Switch result;
        
        @Action
        public void execute()
        {
            result = new Switch();
            for (int i = 0; i < portCount; i++)
                result.portIds.put(i, "port"+ i);
        }
    }
    
    @Scenario
    public static class DeletePort
    {
        @Parameter("Switch")
        @Ref
        public Switch source;
        
        @Parameter
        @Int(min = 1)
        public int portId;
        
        @Parameter(description = "Port is marked as 'Dead'")
        public boolean deadPort;
        
        @Action
        public void execute()
        {
            source.portIds.remove(portId);
        }
    }
    
    @Scenario
    public static class DeleteSwitch
    {
        @Parameter("Switch")
        @Ref
        public Switch source;
        
        @Action
        public void execute() 
        {
            source.portIds.clear();
        }
    }
}









