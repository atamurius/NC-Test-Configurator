package com.netcracker.tc.types.standard.enumeration;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.SimpleTypeReader;

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
        return new EnumType((Class<Enum<?>>) type);
    }
}