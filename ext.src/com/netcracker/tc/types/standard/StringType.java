package com.netcracker.tc.types.standard;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Type;

public class StringType implements Type
{
    private static final ResourceBundle LANG = ResourceBundle.getBundle("types");
    
    private final boolean required;
    
    private final String pattern;
    
    private final int maxLength;

    public StringType(boolean required, int maxLength, String pattern)
    {
        this.required = required;
        this.maxLength = maxLength;
        this.pattern = pattern;
    }

    public StringType()
    {
        this(false, 0, "");
    }

    public boolean isRequired()
    {
        return required;
    }

    public String getPattern()
    {
        return pattern;
    }

    public int getMaxLength()
    {
        return maxLength;
    }

    @Override
    public String validate(Property property)
    {
        Object value = property.getValue(); 

        if (value == null || ! (value instanceof String))
            throw new IllegalArgumentException("String type cannot have values as "+ value);
        
        String string = (String) value;
        
        if (isRequired() && string.isEmpty())
            return LANG.getString("type.required");
        
        if (maxLength > 0 && string.length() > maxLength)
            return MessageFormat.format(LANG.getString("type.string.max_length"), maxLength);
        
        if (! pattern.isEmpty() && ! string.matches(pattern))
            return LANG.getString("type.string.doesnt_match_pattern");
        
        return null;
    }

    @Override
    public String defaultValue()
    {
        return "";
    }

    @Override
    public Object valueOf(String value)
    {
        return value;
    }
}
