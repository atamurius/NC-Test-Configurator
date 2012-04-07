package com.netcracker.sova.types.enumeration;

import java.util.Locale;
import java.util.ResourceBundle;

import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Type;

/**
 * Enum scenario parameter type.
 * Values should be of Enum type.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class EnumType implements Type
{
    private static final ResourceBundle LANG = 
            ResourceBundle.getBundle("types", Locale.getDefault(), EnumType.class.getClassLoader());

    private final Class<? extends Enum<?>> enumType;
    
    private final Object[] values;
    
    private final boolean isRequired;
    
    @Override
    public String validate(Parameter property)
    {
        if (! isRequired && property.getValue() == null)
            return null;
        
        if (! enumType.isInstance(property.getValue()))
            return LANG.getString("type.enum.invalid");
                    
        return null;
    }

    @Override
    public Object defaultValue()
    {
        return null;
    }

    public EnumType(Class<? extends Enum<?>> type)
    {
        this(type, true);
    }

    public EnumType(Class<? extends Enum<?>> type, boolean isRequired)
    {
        this.enumType = type;
        this.isRequired = isRequired;
        try {
            this.values = (Object[]) type.getMethod("values").invoke(null);
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
            if (value.isEmpty())
                return null;
            return enumType.getMethod("valueOf", String.class).invoke(null, value);
        }
        catch (Exception e) {
            // Should not be thrown
            throw new RuntimeException(e);
        }
    }

    public boolean isRequired()
    {
        return isRequired;
    }

    public Class<? extends Enum<?>> getEnumType()
    {
        return enumType;
    }
}
