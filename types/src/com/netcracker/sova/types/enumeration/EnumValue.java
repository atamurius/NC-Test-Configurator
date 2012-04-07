package com.netcracker.sova.types.enumeration;

import com.netcracker.sova.io.xml.ValueWriter;
import com.netcracker.sova.model.Type;
import com.netcracker.util.xml.Constructor;

public class EnumValue implements ValueWriter
{
    @Override
    public boolean writeValue(Constructor xml, Type type, Object value)
    {
        if (type instanceof EnumType) {
            Enum<?> v = (Enum<?>) value;
            xml.attr("value", value == null ? "" : v.name());
            return true;
        }
        else
            return false;
    }
}
