package com.netcracker.sova.model;

import com.netcracker.sova.model.Observer.Event;

/**
 * Scenario input parameter.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 * 
 * @see Scenario
 */
public class Parameter
{
    private final Scenario scenario;

    private final String name;
    
    private final String title;
    
    private final String description;
    
    private final Type type;
    
    private Object value;

    private Parameter(Scenario scenario, 
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

    /**
     * Creates unbound parameter.
     * @param name
     * @param title
     * @param description
     * @param type
     * @param value
     */
    public Parameter(String name, String title, String description, Type type, Object value)
    {
        this(null, name, title, description, type, value);
    }

    /**
     * Creates unbound parameter with default type value.
     * @param name
     * @param title
     * @param description
     * @param type
     */
    public Parameter(String name, String title, String description, Type type)
    {
        this(null, name, title, description, type, type.defaultValue());
    }

    /**
     * Creates binded to scenario copy of parameter.
     * @param scenario
     */
    public Parameter bindedTo(Scenario scenario)
    {
        return new Parameter(scenario, name, title, description, type, value);
    }

    /**
     * Gets parameter scenario (if bound)
     */
    public Scenario getScenario()
    {
        return scenario;
    }

    /**
     * Parameter name (uniq through the scheme) 
     */
    public String getName()
    {
        return name;
    }

    /**
     * Parameter description, optional.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Parameter type with value restrictions.
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Gets value validation error (if any)
     * @return error string or null if value is valid
     * @see #isValid
     * @see Type#validate
     */
    public String getError()
    {
        return type.validate(this);
    }
    
    /**
     * Validates current parameter value for parameter type
     * @return if value is valid
     * @see Type#validate
     * @see #getError
     */
    public boolean isValid()
    {
        return (type.validate(this) == null);
    }
    
    /**
     * Gets current parameter value
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Changes current parameter value.
     * All observers will be notified about {@link Event#CHANGED} of this object
     */
    public void setValue(Object value)
    {
        if (this.value == value)
            return;
        this.value = value;
        if (scenario != null)
            scenario.getParent().getParent().notify(this, Event.CHANGED);
    }

    /**
     * Gets title of parameter, for displaing to user.
     * @return
     */
    public String getTitle()
    {
        return title;
    }
}
