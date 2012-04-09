package com.netcracker.sova.types.pub;

import java.lang.reflect.Field;

import com.netcracker.util.Case;

/**
 * Utility class.
 * 
 * @author dreamer
 */
public class Enums
{
    /**
     * Converts enum value to string, using 
     * case conversion and {@link Label} annotation.
     * EACH_WORD is converted to "Each Word",
     * abbreviations ends with '__':
     * XML__VALUE is converted to "XML Value"
     * @param value enum value
     * @return string representation
     */
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
