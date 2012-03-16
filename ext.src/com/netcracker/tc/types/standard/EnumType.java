package com.netcracker.tc.types.standard;

import java.util.ResourceBundle;

import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Type;

public class EnumType implements Type
{
    private static final ResourceBundle LANG = ResourceBundle.getBundle("types");

    private final Class<? extends Enum<?>> enumType;
    
    private final Object[] values;
    
    @Override
    public String validate(Property property)
    {
        if (! enumType.isInstance(property.getValue()))
            return LANG.getString("type.enum.invalid");
                    
        return null;
    }

    @Override
    public Object defaultValue()
    {
        return null;
    }
    
    public EnumType(Class<? extends Enum<?>> class1)
    {
        this.enumType = class1;
        try {
            this.values = (Object[]) class1.getMethod("values").invoke(null);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public Object[] getValues()
    {
        return values;
    }

    @Override
    public Object valueOf(String value)
    {
        try {
            return enumType.getMethod("valueOf", String.class).invoke(null, value);
        }
        catch (Exception e) {
            // Should not be thrown
            throw new RuntimeException(e);
        }
    }
}
