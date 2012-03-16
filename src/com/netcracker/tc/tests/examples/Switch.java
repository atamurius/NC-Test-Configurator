package com.netcracker.tc.tests.examples;

import java.util.HashMap;
import java.util.Map;

import com.netcracker.tc.tests.anns.Output;
import com.netcracker.tc.tests.anns.Param;
import com.netcracker.tc.tests.anns.Scenario;
import com.netcracker.tc.tests.anns.Scenarios;
import com.netcracker.tc.types.standard.anns.Int;
import com.netcracker.tc.types.standard.anns.Ref;

@Scenarios
public class Switch
{
    public static class SwitchInfo
    {
        public String id;
        public Map<Integer,String> portIds = new HashMap<Integer, String>();
    }
    
    @Scenario(
        output = @Output("Switch")
    )
    public SwitchInfo create(
            @Param("name") String name,
            @Param("portCount") @Int(min = 1) int portCount) {
        
        SwitchInfo result = new SwitchInfo();
        for (int i = 0; i < portCount; i++)
            result.portIds.put(i, "port"+ i);
        return result;
    }
    
    @Scenario
    public void deletePort(
            @Param("switch") @Ref SwitchInfo source,
            @Param("portId") @Int(min = 1) int portId,
            @Param(value = "deadPort", description = "Port is marked as 'Dead'") boolean deadPort) {

        source.portIds.remove(portId);
    }
    
    @Scenario
    public void deleteSwitch(
            @Param("switch") @Ref SwitchInfo source) {

        source.portIds.clear();
    }
}









