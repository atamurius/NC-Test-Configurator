package com.netcracker.tc.model.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Type;

public class EnumType implements Type
{
    private static final ResourceBundle LANG = ResourceBundle.getBundle("types");

    private final Collection<Object> values;
    
    private final Object defaultValue;
    
    @Override
    public String validate(Property property)
    {
        if (! values.contains(property.getValue()))
            return LANG.getString("type.enum.invalid");
                    
        return null;
    }

    @Override
    public Object defaultValue()
    {
        return defaultValue;
    }
    
    public EnumType(Object defValue, Collection<Object> values)
    {
        this.values = values;
        this.defaultValue = defValue;
    }
    
    public Collection<Object> getValues()
    {
        return values;
    }

    public EnumType(Object defValue, Object ... values)
    {
        this(defValue, Arrays.asList(values));
    }
}
