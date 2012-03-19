package com.netcracker.tc.types.standard.enumeration;

import java.lang.annotation.Annotation;

import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;

public final class EnumTypeReader implements TypeReader
{
    @SuppressWarnings("unchecked")
    @Override
    public Type read(Class<?> type, Annotation[] annotations)
    {
        if (Enum.class.isAssignableFrom(type)) {
            return new EnumType((Class<Enum<?>>) type);
        }
        else
            return null;
    }
}