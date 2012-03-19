package com.netcracker.tc.types.standard;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.ref.Ref;

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
            if (tp.isAssignableFrom(type))
                return getType(type, element);
                
        return null;
    }
}
