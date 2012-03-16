package com.netcracker.tc.types.standard.data;

import java.lang.annotation.Annotation;

import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.BoolType;

public final class BoolTypeReader implements TypeReader
{
    @Override
    public Type read(Class<?> type, Annotation[] annotations)
    {
        if (type == Boolean.TYPE || type == Boolean.class)
            return new BoolType();
        else
            return null;
    }
}