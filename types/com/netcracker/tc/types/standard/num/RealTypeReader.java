package com.netcracker.tc.types.standard.num;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.isNaN;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.SimpleTypeReader;

public class RealTypeReader extends SimpleTypeReader
{
    public RealTypeReader()
    {
        super(null, Double.TYPE, Double.class);
    }
    
    @Override
    protected Type getType(Class<?> type, AnnotatedElement element)
    {
        Num a = element.getAnnotation(Num.class);
        return (a == null)
                ? new RealType()
                : new RealType(
                        isNaN(a.min()) ? NEGATIVE_INFINITY : a.min(), 
                        isNaN(a.max()) ? POSITIVE_INFINITY : a.max());
    }
}
