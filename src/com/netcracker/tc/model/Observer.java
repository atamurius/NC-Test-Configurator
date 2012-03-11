package com.netcracker.tc.model;

/**
 * Test group state observer.
 * Events will be triggered if
 * {@link Observer.Event#CHANGED} - if test or test step parameters changed (title or param value)
 * {@link Observer.Event#ADDED} - if new test added, or new step to test added
 * {@link Observer.Event#REMOVED} - if test or test step is removed
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public interface Observer
{
    enum Event { CHANGED, ADDED, REMOVED };

    /**
     * If event is
     * {@link Observer.Event#CHANGED} - source is {@link Test} or test {@link Scenario}, that was changed
     * {@link Observer.Event#ADDED} - source is {@link Test} or test {@link Scenario}, that was added
     * {@link Observer.Event#REMOVED} - source is {@link Test} or test {@link Scenario}, that was removed
     * @param source
     * @param event
     */
    void update(Object source, Event event);
}
