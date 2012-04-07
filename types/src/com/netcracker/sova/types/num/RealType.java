package com.netcracker.sova.types.num;

import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Type;
import com.netcracker.util.Label;
import com.netcracker.util.Label.Bundle;

public class RealType implements Type
{
    private static final Bundle LANG = Label.getBundle(RealType.class, "types");

    private final double min;
    private final double max;
    
    public RealType()
    {
        this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }
    
    public RealType(double min, double max)
    {
        this.min = min;
        this.max = max;
    }

    @Override
    public String validate(Parameter property)
    {
        double value = ((Number) property.getValue()).doubleValue();

        if (min > value || value > max) {
            if (Double.isInfinite(min))
                return LANG.get("type.real.min", max);
            if (Double.isInfinite(max))
                return LANG.get("type.real.max", min);
            else
                return LANG.get("type.real.bounds", min, max);
        }
        
        return null;
    }

    @Override
    public Double defaultValue()
    {
        return Double.isInfinite(min) 
                ? (Double.isInfinite(max)
                        ? 0.0 : max) : min;
    }

    @Override
    public Double valueOf(String value)
    {
        return Double.valueOf(value);
    }

    public double getMin()
    {
        return min;
    }

    public double getMax()
    {
        return max;
    }

}
