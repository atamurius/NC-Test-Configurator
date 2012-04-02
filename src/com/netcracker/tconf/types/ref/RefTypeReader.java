package com.netcracker.tconf.types.ref;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.tconf.annotated.TypeReader;
import com.netcracker.tconf.annotated.anns.Optional;
import com.netcracker.tconf.model.Type;

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