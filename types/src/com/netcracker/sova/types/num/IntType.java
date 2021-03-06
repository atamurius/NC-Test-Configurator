package com.netcracker.sova.types.num;

import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Type;
import com.netcracker.util.Label;
import com.netcracker.util.Label.Bundle;

public class IntType implements Type
{
    private static final Bundle LANG = Label.getBundle(IntType.class, "types");

    private final int min;
    private final int max;

    public IntType(int min, int max)
    {
        this.min = min;
        this.max = max;
    }
    
    public IntType()
    {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public int getMin()
    {
        return min;
    }

    public int getMax()
    {
        return max;
    }

    @Override
    public String validate(Parameter property)
    {
        int integer = ((Number) property.getValue()).intValue(); 

        if (min > integer || integer > max) {
            if (min == Integer.MIN_VALUE)
                return LANG.get("type.int.min", max);
            else if (max == Integer.MAX_VALUE)
                return LANG.get("type.int.max", min);
            else
                return LANG.get("type.int.bounds", min, max);
        }
        
        return null;
    }

    @Override
    public Number defaultValue()
    {
        return (min > 0 ? min : (max < 0 ? max : 0));
    }

    @Override
    public Object valueOf(String value)
    {
        return Integer.valueOf(value);
    }
}
