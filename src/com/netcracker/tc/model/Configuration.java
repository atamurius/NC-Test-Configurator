package com.netcracker.tc.model;

import static com.netcracker.tc.model.Observer.Event.ADDED;
import static com.netcracker.tc.model.Observer.Event.REMOVED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test cases configuration, contains list of test configurations,
 * sends events about configuration changes on all levels 
 * (test cases configuration, test case, test scenario, test scenario parameter).
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Configuration
{
    private final List<Test> tests = new ArrayList<Test>();
    private final List<Test> testsView = Collections.unmodifiableList(tests);
    
    private final Collection<Observer> observers = new HashSet<Observer>();
    
    /**
     * Immutable list of tests.
     * @return tests list
     */
    public List<Test> tests()
    {
        return testsView;
    }
    
    /**
     * Adds new test to group and returns it.
     * @param title of new test
     * @return new test
     */
    public Test add(Test test)
    {
        tests.add(test);
        test.bind(this);
        notify(test, ADDED);
        return test;
    }
    
    /**
     * Removes test from group if it's in it.
     * Test is not valid after that.
     * @param test to remove
     */
    public void remove(Test test)
    {
        tests.remove(test);
        notify(test, REMOVED);
        test.bind(null);
    }
    
    public void clear()
    {
        for (Iterator<Test> tests = this.tests.iterator();
                tests.hasNext();) {
            Test test = tests.next();
            tests.remove();
            test.bind(null);
        }
        notify(this, REMOVED);
    }
    
    /**
     * Adds observer of changes to all test tree,
     * events will triggered for any test or test step of group.
     * @param o
     */
    public void addObserver(Observer o)
    {
        observers.add(o);
    }
    
    /**
     * Delete observer of this group.
     * @param o
     */
    public void deleteObserver(Observer o)
    {
        observers.remove(o);
    }
    
    /**
     * Notify all observers about event of source object.
     * @param src source of event
     * @param event type
     */
    void notify(Object src, Observer.Event event)
    {
        for (Observer obs : observers.toArray(new Observer[0])) {
            obs.update(src, event);
        }
    }
}
