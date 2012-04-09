package com.netcracker.sova.types.string;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.sova.annotated.anns.Optional;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.types.SimpleTypeReader;
import com.netcracker.sova.types.pub.Str;

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
                ? new StringType(! element.isAnnotationPresent(Optional.class))
                : new StringType(
                        ! element.isAnnotationPresent(Optional.class), 
                        a.max(), 
                        a.pattern());
    }
}