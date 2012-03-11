package com.netcracker.tc.model;

import java.util.Collections;
import java.util.List;

/**
 * Prototype for test steps.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Action
{
    private final List<Property> properties;

    private final List<Result> results;
    
    public Action(List<Property> properties, List<Result> results)
    {
        this.properties = Collections.unmodifiableList(properties);
        this.results = Collections.unmodifiableList(results);
    }

    public List<Property> properties()
    {
        return properties;
    }

    public List<Result> results()
    {
        return results;
    }
}
