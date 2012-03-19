package com.netcracker.tc.model;

/**
 * Test configuration state observer.
 * Events will be triggered with:
 * {@link Observer.Event#CHANGED} - if test or scenario parameters changed (title or param value)
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
     * {@link Observer.Event#CHANGED} 
     *      - source is {@link Test}, test {@link Scenario} or {@link Parameter}, that was changed
     * {@link Observer.Event#ADDED} 
     *      - source is {@link Test} or test {@link Scenario}, that was added
     * {@link Observer.Event#REMOVED} 
     *      - source is {@link Test}, test {@link Scenario} or {@link Configuration}, that was removed
     * @param source is {@link Configuration}, {@link Test}, {@link Scenario} or {@link Parameter}
     * @param event
     */
    void update(Object source, Event event);
}
