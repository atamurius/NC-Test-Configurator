package com.netcracker.sova.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototype for test scenarios.
 * Contains avaliable parameters and outputs for scenario.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Schema
{
    private final List<Parameter> parameters;

    private final List<Output> outputs;
    
    private final String name;
    
    private final String title;
    
    /**
     * Creates schema, using lists of parameters and outputs.
     * @param name unique schema name
     * @param title schema title for display
     * @param params list of unbound parameters
     * @param outs list or unbound outputs
     */
    public Schema(String name, String title, List<Parameter> params, List<Output> outs)
    {
        this.name = name;
        this.title = title;
        this.parameters = new ArrayList<Parameter>(params);
        this.outputs = new ArrayList<Output>(outs);
    }

    /**
     * Parameters of test scenario schema.
     */
    public Iterable<Parameter> parameters()
    {
        return parameters;
    }

    /**
     * Outputs of test scenario schema.
     */
    public Iterable<Output> outputs()
    {
        return outputs;
    }

    /**
     * Schema unique name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Schema title for display
     */
    public String getTitle()
    {
        return title;
    }
    
    @Override
    public String toString()
    {
        return "Schema[" + getName() + "]: " + parameters + " -> " + outputs;
    }
}
