package com.netcracker.sova.annotated;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflection utils.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class Reflection
{
    /**
     * Finds all non-static fields (private, default, protected, public)
     * of class and it's supertypes.
     * @param type
     * @return non-static fields of type
     */
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
    
    /**
     * Tries to create instance of class using empty constructor.
     * In case of error just returns null.
     * @param type
     * @return instance of type or null
     */
    static public <T> T maybeCreate(Class<T> type)
    {
        try {
            return type.newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Tries to get field value, in case of error returns null.
     * @param obj
     * @param field
     * @return value of field or null
     */
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

    /**
     * Finds non-static field of type.
     * @param type
     * @param name
     * @return field
     * @throws NoSuchFieldException
     */
    static public Field findField(Class<?> type, String name) throws NoSuchFieldException
    {
        Class<?> cls = type;
        while (type != null) {
            try {
                Field field = cls.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException(type + "." + name);
    }

    static public Method findMethod(Class<?> type, String name) throws NoSuchMethodException
    {
        Method found = null;
        for (Method m : type.getMethods())
            if (m.isAnnotationPresent(com.netcracker.sova.annotated.anns.Scenario.class) && 
                    m.getName().equals(name)) {
                if (found == null)
                    found = m;
                else
                    throw new NoSuchMethodException(type + " must have only one method '"+ name +"'");
            }
        if (found != null)
            return found;
        else
            throw new NoSuchMethodException(type + "." + name);
    }

}
