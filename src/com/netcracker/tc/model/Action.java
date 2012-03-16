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
    
    private final String name;
    
    private final String title;
    
    public Action(String name, String title, List<Property> properties, List<Result> results)
    {
        this.name = name;
        this.title = title;
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

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }
    
    @Override
    public String toString()
    {
        return getClass().getName() + "(" + properties + ", " + results + ")";
    }
}
