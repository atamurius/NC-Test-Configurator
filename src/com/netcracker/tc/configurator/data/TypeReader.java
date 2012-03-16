package com.netcracker.tc.configurator.data;

import java.lang.annotation.Annotation;

import com.netcracker.tc.model.Type;

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
    Type read(Class<?> type, Annotation[] annotations);
}