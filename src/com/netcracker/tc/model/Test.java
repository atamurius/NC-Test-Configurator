package com.netcracker.tc.model;

import static com.netcracker.tc.model.Observer.Event.ADDED;
import static com.netcracker.tc.model.Observer.Event.CHANGED;
import static com.netcracker.tc.model.Observer.Event.REMOVED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collection of test steps,
 * all steps would execute sequentially and thus 
 * can refer to previous step within the same test.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Test
{
    /**
     * Test case title, for humans.
     */
    private String title;
    
    /**
     * List of test case actions.
     */
    final List<Scenario> steps = new ArrayList<Scenario>();
    private final List<Scenario> _steps = Collections.unmodifiableList(steps);

    /**
     * Container test group of current test.
     */
    private TestGroup group;

    /**
     * Creates new test withing given group.
     * Constructor is called from {@link TestGroup#addTest(String)}
     * @param group
     * @param title
     */
    public Test(String title)
    {
        this.title = title;
    }
    
    /**
     * Create new step of this test.
     * @param title of test
     * @param prototype of test
     * @return step created
     */
    public Scenario add(Scenario scenario)
    {
        steps.add(scenario);
        scenario.setTest(this);
        getGroup().notify(scenario, ADDED);
        return scenario;
    }
    
    /**
     * Remove step from test, step if not valid after that.
     * @param step to remove
     */
    public void remove(Scenario step)
    {
        steps.remove(step);
        getGroup().notify(step, REMOVED);
        step.setTest(null);
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
        getGroup().notify(this, CHANGED);
    }

    /**
     * Immutable test steps list
     * @return test steps
     */
    public List<Scenario> scenarios()
    {
        return _steps;
    }
    
    @Override
    public String toString()
    {
        return title;
    }

    public TestGroup getGroup()
    {
        return group;
    }

    void setGroup(TestGroup testGroup)
    {
        this.group = testGroup;
    }

    public boolean isValid()
    {
        for (Scenario s : steps)
            if (! s.isValid())
                return false;
        return true;
    }
}
