package com.netcracker.tc.types.standard.map;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Type;

/**
 * Map string to string type.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class MapType implements Type
{
    @Override
    public String validate(Parameter property)
    {
        if (property.getValue() instanceof Map<?, ?>)
            return null;
        else
            throw new IllegalArgumentException("Map expected, but found "+ property.getValue());
    }

    @Override
    public Object defaultValue()
    {
        return Collections.EMPTY_MAP;
    }

    /**
     * Uses `key:value,key:value` format.
     */
    @Override
    public Object valueOf(String value)
    {
        Map<String,String> map = new LinkedHashMap<String, String>();
        
        for (String pair : value.split(",")) {
            String[] p = pair.trim().split(":");
            if (p.length == 2)
                map.put(p[0].trim(), p[1].trim());
        }
        return map;
    }

}
