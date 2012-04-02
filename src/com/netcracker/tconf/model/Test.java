package com.netcracker.tconf.model;

import static com.netcracker.tconf.model.Observer.Event.ADDED;
import static com.netcracker.tconf.model.Observer.Event.CHANGED;
import static com.netcracker.tconf.model.Observer.Event.REMOVED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One test case configuration, consists of test scenarios.
 * All scenarios would execute sequentially and thus 
 * can refer to previous scenarios within the same test.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Test
{
    private String title;
    
    final List<Scenario> scenarios = new ArrayList<Scenario>();
    private final List<Scenario> scenariosView = Collections.unmodifiableList(scenarios);

    /**
     * Root configuration object.
     */
    private Configuration root;

    /**
     * Creates new unbound test.
     * @param title title of test case
     */
    public Test(String title)
    {
        this.title = title;
    }
    
    /**
     * Adds new scenario to test case
     * @param scenario
     * @return added scenario object
     */
    public Scenario add(Scenario scenario)
    {
        scenarios.add(scenario);
        scenario.bind(this);
        getParent().notify(scenario, ADDED);
        return scenario;
    }
    
    /**
     * Remove scenario from test
     * @param scenario to remove
     */
    public void remove(Scenario scenario)
    {
        scenarios.remove(scenario);
        root.notify(scenario, REMOVED);
        scenario.bind(null);
    }

    /**
     * Gets title of test step
     * @return title of test step
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Changes test step title
     * @param title new title of test step
     */
    public void setTitle(String title)
    {
        this.title = title;
        getParent().notify(this, CHANGED);
    }

    /**
     * Immutable test steps list
     * @return test steps
     */
    public List<Scenario> scenarios()
    {
        return scenariosView;
    }
    
    @Override
    public String toString()
    {
        return title;
    }

    /**
     * Gets parent configuration, containing this test case
     */
    public Configuration getParent()
    {
        return root;
    }

    /**
     * Binds test case to configuration
     */
    void bind(Configuration conf)
    {
        this.root = conf;
    }

    /**
     * Checks if all test case scenarios has valid parameter values
     * @return this if test case is valid
     */
    public boolean isValid()
    {
        for (Scenario s : scenarios)
            if (! s.isValid())
                return false;
        return true;
    }

    /**
     * Notify all observers about event of source object.
     * @param src source of event
     * @param event type
     */
    void notify(Object src, Observer.Event event)
    {
        if (root != null)
            root.notify(src, event);
    }
}
