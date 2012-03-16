package com.netcracker.tc.types.standard.data;

import java.lang.annotation.Annotation;

import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.EnumType;

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