package com.netcracker.tc.model;

import com.netcracker.tc.model.Observer.Event;

/**
 * Scenario property
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 *
 */
public class Property
{
    private final Scenario scenario;

    private final String name;
    
    private final String title;
    
    private final String description;
    
    private final Type type;
    
    private Object value;

    private Property(Scenario scenario, 
            String name, String title, String description, 
            Type type, Object value)
    {
        this.scenario = scenario;
        this.title = title;
        this.name = name;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    public Property(String name, String title, String description, Type type, Object value)
    {
        this(null, name, title, description, type, value);
    }

    public Property(String name, String title, String description, Type type)
    {
        this(null, name, title, description, type, type.defaultValue());
    }

    Property(Scenario scenario, Property p)
    {
        this(scenario, p.name, p.title, p.description, p.type, p.value);
    }

    public Scenario getScenario()
    {
        return scenario;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public Type getType()
    {
        return type;
    }

    public String getError()
    {
        return type.validate(this);
    }
    
    public boolean isValid()
    {
        return (type.validate(this) == null);
    }
    
    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        if (this.value == value)
            return;
        this.value = value;
        if (scenario != null)
            scenario.getTest().getGroup().notify(this, Event.CHANGED);
    }

    public String getTitle()
    {
        return title;
    }
}
