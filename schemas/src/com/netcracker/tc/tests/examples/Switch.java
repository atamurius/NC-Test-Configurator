package com.netcracker.tc.tests.examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netcracker.tc.tests.anns.Output;
import com.netcracker.tc.tests.anns.Param;
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
            @Param("portCount") @Num(min = 1) int portCount,
            @Param(value = "portNames", description = "One name per row", defValue = "port1,port2") 
            List<String> names) {
        
        SwitchInfo result = new SwitchInfo();
        for (int i = 0; i < portCount; i++)
            result.portIds.put(i, "port"+ i);
        return result;
    }
    
    @Scenario
    public void deletePort(
            @Param("switch") @Ref SwitchInfo source,
            @Param("portId") @Num(min = 1) int portId,
            @Param(value = "deadPort", description = "Port is marked as 'Dead'") boolean deadPort) {

        source.portIds.remove(portId);
    }
    
    @Scenario
    public void deleteSwitch(
            @Param("switch") @Ref SwitchInfo source) {

        source.portIds.clear();
    }
}









