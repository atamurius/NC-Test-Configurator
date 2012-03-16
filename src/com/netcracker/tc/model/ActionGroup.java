package com.netcracker.tc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActionGroup
{
    private final Map<String,List<Action>> actions = new LinkedHashMap<String, List<Action>>();

    public void add(String group, Action action)
    {
        if (! actions.containsKey(group))
            actions.put(group, new ArrayList<Action>());
        actions.get(group).add(action);
    }
    
    public Iterable<String> groups()
    {
        return actions.keySet();
    }
    
    public Iterable<Action> actions(String group)
    {
        return actions.get(group);
    }
    
    public Action find(String name)
    {
        for (String group : groups())
            for (Action action : actions(group))
                if (name.equals(action.getName()))
                    return action;
        return null;
    }
    
    @Override
    public String toString()
    {
        return getClass().getName() + actions;
    }
}
