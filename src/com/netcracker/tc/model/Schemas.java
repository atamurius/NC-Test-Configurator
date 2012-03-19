package com.netcracker.tc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases schemas.
 * Contains schemas, collected into named groups.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Schemas
{
    private final Map<String,List<Schema>> actions = new LinkedHashMap<String, List<Schema>>();

    /**
     * Adds schema
     * @param group title for this schema
     * @param schema
     */
    public void add(String group, Schema schema)
    {
        if (! actions.containsKey(group))
            actions.put(group, new ArrayList<Schema>());
        actions.get(group).add(schema);
    }
    
    /**
     * Current schema groups titles
     */
    public Iterable<String> groups()
    {
        return actions.keySet();
    }
    
    /**
     * Schemas in given group
     */
    public Iterable<Schema> group(String group)
    {
        return actions.get(group);
    }
    
    /**
     * Finds schema by it's name
     * @param name name of schema to search
     * @return schema, such that it.getName().equals(name) or null if none
     */
    public Schema find(String name)
    {
        for (String group : groups())
            for (Schema schema : group(group))
                if (name.equals(schema.getName()))
                    return schema;
        return null;
    }
    
    @Override
    public String toString()
    {
        return actions.toString();
    }
}
