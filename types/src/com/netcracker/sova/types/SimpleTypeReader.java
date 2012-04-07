package com.netcracker.sova.types;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import com.netcracker.sova.annotated.TypeReader;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.types.ref.Ref;

public abstract class SimpleTypeReader implements TypeReader
{
    protected final Class<?>[] types;
    protected final Type type;
    
    public SimpleTypeReader(Type type, Class<?> ... types)
    {
        this.types = types;
        this.type = type;
    }
    
    protected Type getType(Class<?> type, AnnotatedElement element)
    {
        return this.type;
    }

    @Override
    public Type read(Class<?> type, AnnotatedElement element)
    {
        if (element.isAnnotationPresent(Ref.class))
            return null;

        for (Class<?> tp : types)
            if (tp.isAssignableFrom(type)) {
                return getType(type, element);
            }
                
        return null;
    }
}
