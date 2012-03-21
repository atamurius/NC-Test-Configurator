package com.netcracker.tc.types.standard.num;

import static java.lang.Double.isNaN;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.SimpleTypeReader;

public final class IntTypeReader extends SimpleTypeReader
{
    public IntTypeReader()
    {
        super(null, Integer.TYPE, Integer.class);
    }
    
    @Override
    protected Type getType(Class<?> type, AnnotatedElement element)
    {
        if (element.isAnnotationPresent(Num.class)) {
            Num a = element.getAnnotation(Num.class);
            return new IntType(
                        isNaN(a.min()) ? MIN_VALUE : (int) a.min(), 
                        isNaN(a.max()) ? MAX_VALUE : (int) a.max());
        }
        else
            return new IntType();
    }
}