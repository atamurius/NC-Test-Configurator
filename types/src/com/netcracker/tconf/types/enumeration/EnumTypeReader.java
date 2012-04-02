package com.netcracker.tconf.types.enumeration;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tconf.annotated.anns.Optional;
import com.netcracker.tconf.model.Type;
import com.netcracker.tconf.types.SimpleTypeReader;

public final class EnumTypeReader extends SimpleTypeReader
{
    public EnumTypeReader()
    {
        super(null, Enum.class);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected Type getType(Class<?> type, AnnotatedElement element)
    {
        return new EnumType(
                (Class<Enum<?>>) type, 
                ! element.isAnnotationPresent(Optional.class));
    }
}