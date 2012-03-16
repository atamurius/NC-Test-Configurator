package com.netcracker.tc.types.standard.data;

import java.lang.annotation.Annotation;

import com.netcracker.tc.configurator.data.AnnotationSchemaReader;
import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.IntType;
import com.netcracker.tc.types.standard.anns.Int;

public final class IntTypeReader implements TypeReader
{
    @Override
    public Type read(Class<?> type, Annotation[] annotations)
    {
        if (type == Integer.TYPE || type == Integer.class) {
            
            Int a = AnnotationSchemaReader.findAnnotation(annotations, Int.class);
            return (a == null)
                    ? new IntType()
                    : new IntType(a.min(), a.max());
        }
        else
            return null;
    }
}