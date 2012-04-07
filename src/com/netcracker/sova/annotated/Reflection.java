package com.netcracker.sova.annotated;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Reflection
{
    static public List<Field> nonstaticFieldsOf(Class<?> type)
    {
        List<Field> fields = new ArrayList<Field>();
        
        while (type != null) {
            for (Field f : type.getDeclaredFields())
                if (! Modifier.isStatic(f.getModifiers()))
                    fields.add(f);
            type = type.getSuperclass();
        }
        
        return fields;
    }
    
    static public <T> T maybeCreate(Class<T> type)
    {
        try {
            return type.newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    static public Object maybeGet(Object obj, Field field)
    {
        try {
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception e) {
            return null;
        }
    }

}
