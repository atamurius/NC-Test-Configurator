package com.netcracker.sova.annotated;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import com.netcracker.sova.model.Type;
import com.netcracker.util.ClassRegistry;

public class TypeReaders implements ClassRegistry
{
    private final List<TypeReader> readers = new ArrayList<TypeReader>();

    public void register(Class<?> type)
    {
        try {
            if (TypeReader.class.isAssignableFrom(type)) {
                readers.add(type.asSubclass(TypeReader.class).newInstance());
                System.out.println("AnnotationSchemaReader: registered type: "+ type);
            }
        }
        catch (Exception e) {
            System.out.println("AnnotationSchemaReader: Failed to load "+ type);
        }
    }
    
    public Type readType(String name, Class<?> type, AnnotatedElement elem)
    {
        for (TypeReader reader : readers) {
            Type t = reader.read(type, elem);
            if (t != null)
                return t;
        }
        throw new SchemaFormatException("Unknown type for "+ name + ":" + type);
    }
}
