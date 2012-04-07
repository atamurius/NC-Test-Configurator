package com.netcracker.sova.types.anns;

import java.lang.reflect.Field;

import com.netcracker.sova.annotated.Case;

public class EnumLabel
{
    static public String toString(Enum<?> value)
    {
        if (value == null)
            return null;
        
        try {
            Class<?> type = value.getClass();
            Field field = type.getField(value.name());
            if (field.isAnnotationPresent(Label.class))
                return field.getAnnotation(Label.class).value();
            else
                return Case.fromConst(value.name());
        }
        catch (Exception e) {
            // cant be!
            throw new RuntimeException(e);
        }
    }
}
