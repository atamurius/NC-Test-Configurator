package com.netcracker.tconf.examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netcracker.sova.annotated.anns.Default;
import com.netcracker.sova.annotated.anns.Output;
import com.netcracker.sova.annotated.anns.Param;
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
public class Switch
{
    public static class SwitchInfo
    {
        public String id;
        public Map<Integer,String> portIds = new HashMap<Integer, String>();
    }
    
    @Scenario
    @Output("Switch")
    public SwitchInfo create(
            @Param("Name") 
            String name,
            @Param("Port count") @Num(min = 1) @Default("10") 
            int portCount,
            @Param(value = "Port names", description = "One name per row") @Default("port1,port2") 
            List<String> names) {
        
        System.out.printf("Switch.create(name = %s, portCount = %d, names = %s)%n",
                name, portCount, names);
        
        SwitchInfo result = new SwitchInfo();
        for (int i = 0; i < portCount; i++)
            result.portIds.put(i, "port"+ i);
        return result;
    }
    
    @Scenario
    public void deletePort(
            @Param("Switch") @Ref SwitchInfo source,
            @Param("Port ID") @Num(min = 1) int portId,
            @Param(value = "Dead port", description = "Port is marked as 'Dead'") boolean deadPort) {

        System.out.println("Switch.deletePort()");
        
        source.portIds.remove(portId);
    }
    
    @Scenario
    public void deleteSwitch(
            @Param("switch") @Ref SwitchInfo source) {

        source.portIds.clear();
    }
}









