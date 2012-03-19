package com.netcracker.tc.types.standard.string;

import static com.netcracker.tc.configurator.data.AnnotationSchemaReader.findAnnotation;

import java.lang.annotation.Annotation;

import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.ref.Ref;

public final class StringTypeReader implements TypeReader
{
    @Override
    public Type read(Class<?> type, Annotation[] annotations)
    {
        if (type == String.class && findAnnotation(annotations, Ref.class) == null) {
            
            Str a = findAnnotation(annotations, Str.class);
            return (a == null)
                    ? new StringType()
                    : new StringType(a.required(), a.max(), a.pattern());
        }
        else
            return null;
    }
}