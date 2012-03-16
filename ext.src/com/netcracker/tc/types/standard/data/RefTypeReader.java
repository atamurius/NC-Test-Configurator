package com.netcracker.tc.types.standard.data;

import java.lang.annotation.Annotation;

import com.netcracker.tc.configurator.data.AnnotationSchemaReader;
import com.netcracker.tc.configurator.data.TypeReader;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.RefType;
import com.netcracker.tc.types.standard.anns.Ref;

public final class RefTypeReader implements TypeReader
{
    @Override
    public Type read(Class<?> type, Annotation[] annotations)
    {
        Ref a = AnnotationSchemaReader.findAnnotation(annotations, Ref.class);
        if (a != null)
            return new RefType(
                    a.value().isEmpty() ? type.getName() : a.value(), 
                    a.required());
        else
            return null;
    }
}