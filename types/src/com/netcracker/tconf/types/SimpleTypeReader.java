package com.netcracker.tconf.types;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tconf.annotated.TypeReader;
import com.netcracker.tconf.model.Type;
import com.netcracker.tconf.types.ref.Ref;

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
