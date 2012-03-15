package com.netcracker.tc.types.standard;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Type;

public class IntType implements Type
{
    private static final ResourceBundle LANG = ResourceBundle.getBundle("types");

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
    public String validate(Property property)
    {
        Object value = property.getValue(); 

        if (value == null || ! (value instanceof Integer))
            throw new IllegalArgumentException("Invalid integer value "+ value);
        
        int integer = (Integer) value;
        
        if (min > integer || integer > max) {
            if (min == Integer.MIN_VALUE)
                return MessageFormat.format(LANG.getString("type.int.min"), max);
            else if (max == Integer.MAX_VALUE)
                return MessageFormat.format(LANG.getString("type.int.max"), min);
            else
                return MessageFormat.format(LANG.getString("type.int.bounds"), min, max);
        }
        
        return null;
    }

    @Override
    public Integer defaultValue()
    {
        return (min > 0 ? min : (max < 0 ? max : 0));
    }
}
