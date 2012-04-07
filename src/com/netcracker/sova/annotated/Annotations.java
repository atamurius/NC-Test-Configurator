package com.netcracker.sova.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class Annotations implements AnnotatedElement
{
    private final Annotation[] anns;

    public Annotations(Annotation[] anns)
    {
        this.anns = anns;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
    {
        for (Annotation a : anns)
            if (annotationClass.isInstance(a))
                return (T) a;
        return null;
    }

    @Override
    public Annotation[] getAnnotations()
    {
        return anns;
    }

    @Override
    public Annotation[] getDeclaredAnnotations()
    {
        return anns;
    }

    @Override
    public boolean isAnnotationPresent(
            Class<? extends Annotation> annotationClass)
    {
        for (Annotation a : anns)
            if (annotationClass.isInstance(a))
                return true;
        return false;
    }
    
}
