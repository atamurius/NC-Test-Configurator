package com.netcracker.tc.model.types;

import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Result;
import com.netcracker.tc.model.Type;
import com.netcracker.util.Label;

public class RefType implements Type
{
    private static final String LABEL = "types";

    private final String type;
    
    public RefType(String type)
    {
        this.type = type;
    }

    @Override
    public String validate(Property property)
    {
        Result value = (Result) property.getValue();
        if (value == null)
            return Label.get(LABEL, "type.required");
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
}
