package com.netcracker.tconf.types.ref;

import com.netcracker.tconf.model.Output;
import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Type;
import com.netcracker.util.Label;

public class RefType implements Type
{
    private static final String LABEL = "ui";

    private final String type;
    private final boolean required;
    
    public RefType(String type, boolean required)
    {
        this.type = type;
        this.required = required;
    }

    @Override
    public String validate(Parameter property)
    {
        Output value = (Output) property.getValue();
        if (value == null && isRequired())
            return Label.get(LABEL, "type.required");
        if (value == null && ! isRequired())
            return null;
        
        if (! value.getType().equals(type))
            return Label.get(LABEL, "type.ref.invalid_type", type);
        if (! value.getScenario().before(property.getScenario()))
            return Label.get(LABEL, "type.ref.invisible");
        
        return null;
    }

    @Override
    public Object defaultValue()
    {
        return null;
    }

    public String getType()
    {
        return type;
    }

    public boolean isRequired()
    {
        return required;
    }

    @Override
    public Object valueOf(String value)
    {
        throw new UnsupportedOperationException("Reference could not have default value");
    }
}
