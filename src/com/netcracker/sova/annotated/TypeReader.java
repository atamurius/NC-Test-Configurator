package com.netcracker.sova.annotated;

import java.lang.reflect.AnnotatedElement;

import com.netcracker.sova.model.Type;

/**
 * Annotation schema reading module.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public interface TypeReader
{
    /**
     * Tries to read parameter type,
     * using parameter class and set of annotations.
     * @return parameter type or null, if this reader don't know this type.
     */
    Type read(Class<?> type, AnnotatedElement element);
}