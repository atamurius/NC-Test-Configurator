package com.netcracker.tc.types.standard.num;

import java.lang.annotation.Annotation;
import static java.lang.Double.isNaN;
import static java.lang.Integer.*;
import com.netcracker.tc.configurator.data.AnnotationSchemaReader;
import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;

public final class IntTypeReader implements TypeReader
{
    @Override
    public Type read(Class<?> type, Annotation[] annotations)
    {
        if (type == Integer.TYPE || type == Integer.class) {
            
            Num a = AnnotationSchemaReader.findAnnotation(annotations, Num.class);
            return (a == null)
                    ? new IntType()
                    : new IntType(
                            isNaN(a.min()) ? MIN_VALUE : (int) a.min(), 
                            isNaN(a.max()) ? MAX_VALUE : (int) a.max());
        }
        else
            return null;
    }
}