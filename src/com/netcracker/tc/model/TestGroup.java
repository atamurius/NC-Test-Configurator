package com.netcracker.tc.model;

import static com.netcracker.tc.model.Observer.Event.ADDED;
import static com.netcracker.tc.model.Observer.Event.REMOVED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TestGroup
{
    private final List<Test> tests = new ArrayList<Test>();
    private final List<Test> _tests = Collections.unmodifiableList(tests);
    
    private final Collection<Observer> observers = new HashSet<Observer>();
    
    /**
     * Immutable list of group tests.
     * @return tests list
     */
    public List<Test> tests()
    {
        return _tests;
    }
    
    /**
     * Adds new test to group and returns it.
     * @param title of new test
     * @return new test
     */
    public Test add(Test test)
    {
        tests.add(test);
        test.setGroup(this);
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
        test.setGroup(null);
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
        for (Observer obs : observers) {
            obs.update(src, event);
        }
    }
}
