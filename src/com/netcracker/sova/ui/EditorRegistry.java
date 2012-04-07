package com.netcracker.sova.ui;

import java.util.HashMap;
import java.util.Map;

import com.netcracker.sova.model.Parameter;
import com.netcracker.util.ClassRegistry;

public class EditorRegistry implements ClassRegistry
{
    private Map<Class<?>,Class<?>> reg = new HashMap<Class<?>, Class<?>>();
    
    public void register(Class<?> type)
    {
        if (EditorWidget.class.isAssignableFrom(type)) {
            try {
                EditorWidget widget = type.asSubclass(EditorWidget.class).newInstance();
                reg.put(widget.getType(), widget.getClass());
                System.out.println("EditorRegistry: widget registered: "+ type.getName());
            }
            catch (Exception e) {
                System.out.println("EditorRegistry: Failed to load "+ type);
                e.printStackTrace();
            }
        }
    }
    
    public EditorWidget editorFor(Parameter property)
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
