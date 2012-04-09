package com.netcracker.sova.types.ref;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.sova.annotated.TypeReader;
import com.netcracker.sova.annotated.anns.Optional;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.types.pub.Ref;

public final class RefTypeReader implements TypeReader
{
    @Override
    public Type read(Class<?> type, AnnotatedElement elem)
    {
        Ref a = elem.getAnnotation(Ref.class);
        if (a != null)
            return new RefType(
                    a.value().isEmpty() ? type.getName() : a.value(), 
                    ! elem.isAnnotationPresent(Optional.class));
        else
            return null;
    }
}