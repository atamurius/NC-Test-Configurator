package com.netcracker.tconf.types.string;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tconf.annotated.anns.Optional;
import com.netcracker.tconf.model.Type;
import com.netcracker.tconf.types.SimpleTypeReader;

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