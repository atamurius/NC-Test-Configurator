package com.netcracker.tconf.types.enumeration;

import com.netcracker.tconf.io.xml.ValueWriter;
import com.netcracker.tconf.model.Type;
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
