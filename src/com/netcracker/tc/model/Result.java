package com.netcracker.tc.model;

/**
 * Test case action result,
 * can be referensed as parameter value for
 * further actions within this test case.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Result
{
    private final Scenario scenario;
    
    private final String name;
    
    /**
     * Result title, for humans.
     */
    private final String title;
    
    /**
     * Value type, for connecting with parameters of same type.
     */
    private final String type;

    public Result(String name, String title, String type)
    {
        this(null, name, title, type);
    }
    
    Result(Scenario scenario, Result r)
    {
        this(scenario, r.name, r.title, r.type);
    }

    private Result(Scenario scenario, String name, String title, String type)
    {
        this.scenario = scenario;
        this.name = name;
        this.title = title;
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public Scenario getScenario()
    {
        return scenario;
    }
    
    @Override
    public String toString()
    {
        return scenario.getTitle() + ": " + title;
    }
}
