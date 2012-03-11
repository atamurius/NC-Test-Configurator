package com.netcracker.tc.model.types;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;

import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Type;

public class SetType implements Type
{
    private static final ResourceBundle LANG = ResourceBundle.getBundle("types");

    private final Collection<Object> values;

    @Override
    public String validate(Property property)
    {
        Object value = property.getValue(); 
        if (value instanceof Set<?>) {
            Set<?> values = (Set<?>) value;
            for (Object v : values)
                if (! this.values.contains(v))
                    return MessageFormat.format(LANG.getString("type.set.invalid_member"), v);
            return null;
        }
        else
            throw new IllegalArgumentException("Invalid set value: "+ value);
    }

    @Override
    public Object defaultValue()
    {
        return Collections.EMPTY_SET;
    }

    public SetType(Collection<Object> values)
    {
        this.values = values;
    }
    
    public Collection<Object> getValues()
    {
        return values;
    }

    public SetType(Object ... values)
    {
        this(Arrays.asList(values));
    }
}
