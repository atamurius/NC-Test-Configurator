package com.netcracker.tc.types.standard.num;

import static java.lang.Double.*;

import java.lang.annotation.Annotation;

import com.netcracker.tc.configurator.data.AnnotationSchemaReader;
import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;

public class RealTypeReader implements TypeReader
{
    @Override
    public Type read(Class<?> type, Annotation[] annotations)
    {
        if (type == Double.TYPE || type == Double.class) {
            
            Num a = AnnotationSchemaReader.findAnnotation(annotations, Num.class);
            return (a == null)
                    ? new RealType()
                    : new RealType(
                            isNaN(a.min()) ? NEGATIVE_INFINITY : a.min(), 
                            isNaN(a.max()) ? POSITIVE_INFINITY : a.max());
        }
        else
            return null;
    }
}
