package com.netcracker.sova.model;

import com.netcracker.sova.types.ref.RefType;

/**
 * Test scenario output result,
 * can be referensed as parameter value for
 * further scenarios within this test case.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 * 
 * @see Scenario
 * @see RefType 
 */
public class Output
{
    private final Scenario scenario;
    
    private final String name;
    
    private final String title;
    
    private final String type;

    private Object value;
    
    /**
     * Creates unbound output.
     * @param name
     * @param title
     * @param type output type identifier, output can be referenced from parameter only if types are equal
     */
    public Output(String name, String title, String type)
    {
        this(null, name, title, type);
    }
    
    /**
     * Creates binded to scenario copy of output.
     * @param scenario
     */
    public Output bindedTo(Scenario scenario)
    {
        return new Output(scenario, name, title, type);
    }

    private Output(Scenario scenario, String name, String title, String type)
    {
        this.scenario = scenario;
        this.name = name;
        this.title = title;
        this.type = type;
    }

    /**
     * Output title for client.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Output type label, for type checking in parameter referencing
     */
    public String getType()
    {
        return type;
    }

    /**
     * Schema-unique output name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Bounded scenario if any
     */
    public Scenario getScenario()
    {
        return scenario;
    }
    
    @Override
    public String toString()
    {
        return (scenario == null ? "" : scenario.getTitle() + ": ") + title;
    }

    /**
     * For scenario execution only!
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * For scenario execution only!
     */
    public void setValue(Object value)
    {
        this.value = value;
    }
}
