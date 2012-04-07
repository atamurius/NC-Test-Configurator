package com.netcracker.sova.model;

import static com.netcracker.sova.model.Observer.Event.CHANGED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.netcracker.sova.model.Observer.Event;

/**
 * Atomic test step, describes test scenario,
 * that has input parameters and output results.
 * Belongs to one test case.
 * Each step has some prototype {@link Schema},
 * that describes parameters and result that can have this step.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Scenario
{
    private Test test;
    
    private String title;
    
    private final Schema prototype;
    
    private final Map<String, Parameter> parameters;
    
    private final Map<String, Output> outputs;
    
    /**
     * Creates new scenario using given schema.
     * Sets scenario title to schema title.
     * All parameters and outputs are copied and bound to scenario.
     * @param proto prototype schema
     */
    public Scenario(Schema proto)
    {
        this.title = proto.getTitle();
        this.prototype = proto;
        
        Map<String,Parameter> params = new LinkedHashMap<String, Parameter>();
        for (Parameter param : proto.parameters()) {
            params.put(param.getName(), param.bindedTo(this));
        }
        parameters = Collections.unmodifiableMap(params);
                
        Map<String,Output> outs = new LinkedHashMap<String, Output>();
        for (Output out : proto.outputs()) {
            outs.put(out.getName(), out.bindedTo(this));
        }
        outputs = Collections.unmodifiableMap(outs);
    }

    /**
     * Gets step title.
     * @return step title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Changes step title
     * @param title new title of step
     */
    public void setTitle(String title)
    {
        this.title = title;
        if (test != null)
            test.notify(this, CHANGED);
    }

    /**
     * Gets step container test.
     * @return test of current step
     */
    public Test getParent()
    {
        return test;
    }
    
    /**
     * Checks if given object is this scenario or it's parameter.
     * @param obj to check
     * @return true if obj is this or one of parameters
     */
    public boolean isThisOrChild(Object obj)
    {
        return (obj == this) || parameters.containsValue(obj); 
    }
    
    /**
     * Gets step prototype action,
     * can be used to determine step params and results.
     * @return prototype action
     */
    public Schema getSchema()
    {
        return prototype;
    }

    @Override
    public String toString()
    {
        return title;
    }

    /**
     * Binds scenario to test.
     * @param test
     */
    void bind(Test test)
    {
        this.test = test;
    }

    /**
     * This scenario properties.
     * Immutable.
     */
    public Map<String, Parameter> parameters()
    {
        return parameters;
    }

    /**
     * This scenario outputs.
     * Immutable.
     */
    public Map<String, Output> outputs()
    {
        return outputs;
    }

    /**
     * Checks if all scenario parameter has valid values. 
     */
    public boolean isValid()
    {
        for (Parameter prop : parameters.values()) {
            if (! prop.isValid())
                return false;
        }
        return true;
    }

    /**
     * Checks if this scenario will be executed before given withing same test case.
     */
    public boolean before(Scenario scenario)
    {
        if (this.getParent() != scenario.getParent()) {
            return false;
        }
        else {
            return getIndex() < scenario.getIndex();
        }
    }

    /**
     * Gets outputs of all scenarios, that are executed before this scenario within
     * same test case.
     * @param type type label of output to filter
     * @return outputs of given type, that can be referenced in this scenario parameters
     */
    public List<Output> getVisibleResults(String type)
    {
        List<Output> res = new ArrayList<Output>();
        
        for (Scenario s : test.scenarios()) {
            if (s == this) {
                break;
            }
            for (Output r : s.outputs().values()) {
                if (r.getType().equals(type)) {
                    res.add(r);
                }
            }
        }
        return res;
    }

    /**
     * Changes current scenario execution order by given delta.
     * If dalta is out of bounds nothing happens. 
     * @param delta positive or negative number of positions to move this scenario withing it's test case
     */
    public void moveBy(int delta)
    {
        int index = getIndex() + delta;
        if (index >= 0 && index < test.scenarios.size()) {
            test.scenarios.remove(this);
            test.scenarios.add(index, this);
            test.getParent().notify(this, Event.ADDED);
        }
    }

    /**
     * Gets execution order of this scenario in it's test case.
     * 0 is first scenario.
     * @return index of this scenario in it's test case scenario list
     */
    public int getIndex()
    {
        return test.scenarios.indexOf(this);
    }
}






