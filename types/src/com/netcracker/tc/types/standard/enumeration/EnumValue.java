package com.netcracker.tc.types.standard.enumeration;

import com.netcracker.tc.configurator.data.xml.ValueWriter;
import com.netcracker.tc.model.Type;
import com.netcracker.util.xml.Constructor;

public class EnumValue implements ValueWriter
{
    @Override
    public boolean writeValue(Constructor xml, Type type, Object value)
    {
        if (type instanceof EnumType) {
            xml.attr("value", ((Enum<?>) value).name());
            return true;
        }
        else
            return false;
    }
}
