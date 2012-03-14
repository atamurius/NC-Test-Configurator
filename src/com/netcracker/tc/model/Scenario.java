package com.netcracker.tc.model;

import static com.netcracker.tc.model.Observer.Event.CHANGED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.netcracker.tc.model.Observer.Event;

/**
 * Atomic test step, describes test procedure,
 * that has input parameters and output results.
 * Belongs to one test.
 * Each step has some prototype {@link Action},
 * that describes parameters and result that can have this step.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Scenario
{
    /**
     * Parent test case.
     */
    private Test test;
    
    /**
     * Title of action, for humans.
     */
    private String title;
    
    /**
     * Step prototype, describes params and results of step.
     */
    private final Action prototype;
    
    /**
     * Scenario properties
     */
    private final Map<String, Property> properties;
    
    /**
     * Scenario results
     */
    private final Map<String, Result> results;
    
    /**
     * Creates new action, action can only be created through {@link Test#addStep(String, Action)}
     * @param test of step
     * @param title of step
     * @param proto prototype action
     */
    public Scenario(Action proto)
    {
        this.title = proto.getTitle();
        this.prototype = proto;
        
        Map<String,Property> props = new LinkedHashMap<String, Property>();
        for (Property prop : proto.properties())
            props.put(prop.getName(), new Property(this, prop));
        properties = Collections.unmodifiableMap(props);
                
        Map<String,Result> ress = new LinkedHashMap<String, Result>();
        for (Result res : proto.results())
            ress.put(res.getName(), new Result(this, res));
        results = Collections.unmodifiableMap(ress);
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
            test.getGroup().notify(this, CHANGED);
    }

    /**
     * Gets step container test.
     * @return test of current step
     */
    public Test getTest()
    {
        return test;
    }
    
    public boolean isThisOrChild(Object obj)
    {
        return (obj == this) || properties.containsValue(obj); 
    }
    
    /**
     * Gets step prototype action,
     * can be used to determine step params and results.
     * @return prototype action
     */
    public Action getPrototype()
    {
        return prototype;
    }

    @Override
    public String toString()
    {
        return title;
    }

    void setTest(Test test)
    {
        this.test = test;
    }

    public Map<String, Property> properties()
    {
        return properties;
    }

    public Map<String, Result> results()
    {
        return results;
    }

    public boolean isValid()
    {
        for (Property prop : properties.values()) {
            if (! prop.isValid())
                return false;
        }
        return true;
    }

    public boolean before(Scenario scenario)
    {
        if (this.getTest() != scenario.getTest())
            return false;
        else {
            return getIndex() < scenario.getIndex();
        }
    }

    public List<Result> getVisibleResults(String type)
    {
        List<Result> res = new ArrayList<Result>();
        for (Scenario s : test.scenarios()) {
            if (s == this)
                break;
            for (Result r : s.results().values())
                if (r.getType().equals(type))
                    res.add(r);
        }
        return res;
    }

    public void moveBy(int delta)
    {
        int index = getIndex() + delta;
        if (index >= 0 && index < test.steps.size()) {
            test.steps.remove(this);
            test.steps.add(index, this);
            test.getGroup().notify(this, Event.ADDED);
        }
    }

    public int getIndex()
    {
        return test.steps.indexOf(this);
    }
}






