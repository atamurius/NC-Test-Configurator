package com.netcracker.tc.types.standard;

import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Type;

public class BoolType implements Type
{
    @Override
    public String validate(Property property)
    {
        Object value = property.getValue(); 
        if (value instanceof Boolean)
            return null;
        else
            throw new IllegalArgumentException("Invalid boolean value "+ value);
    }

    @Override
    public Boolean defaultValue()
    {
        return false;
    }
}
