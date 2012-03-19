package com.netcracker.tc.types.standard.string;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.SimpleTypeReader;

public final class StringTypeReader extends SimpleTypeReader
{
    public StringTypeReader()
    {
        super(null, String.class);
    }
    
    @Override
    protected Type getType(Class<?> type, AnnotatedElement element)
    {
        Str a = element.getAnnotation(Str.class);
        return (a == null)
                ? new StringType()
                : new StringType(a.required(), a.max(), a.pattern());
    }
}