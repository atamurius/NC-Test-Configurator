package com.netcracker.tc.types.standard.bool;

import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Type;

/**
 * Boolean scenario parameter type.
 * Values should be of Boolean type.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class BoolType implements Type
{
    @Override
    public String validate(Parameter property)
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

    @Override
    public Object valueOf(String value)
    {
        return Boolean.valueOf(value);
    }
}