package com.netcracker.tc.configurator.ui;

import java.util.HashMap;
import java.util.Map;

import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Type;

public class EditorRegistry
{
    private Map<Class<?>,Class<?>> reg = new HashMap<Class<?>, Class<?>>();
    
    public void register(Class<? extends Type> ptype, Class<? extends EditorWidget> wtype)
    {
        reg.put(ptype, wtype);
    }
    
    public EditorWidget editorFor(Property property)
    {
        Class<?> wtype = reg.get(property.getType().getClass());
        if (wtype == null)
            throw new IllegalArgumentException("Unregistered property type: "+ property.getType());
        try {
            EditorWidget widget = (EditorWidget) wtype.newInstance();
            widget.setProperty(property);
            return widget;
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
