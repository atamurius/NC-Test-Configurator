package com.netcracker.tconf.types.list;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Type;

/**
 * List of strings.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public class ListType implements Type
{
    @Override
    public String validate(Parameter property)
    {
        if (property.getValue() instanceof List<?>)
            return null;
        else
            throw new IllegalArgumentException("ListType value should be instance of List");
    }

    @Override
    public List<?> defaultValue()
    {
        return Collections.EMPTY_LIST;
    }

    /**
     * Breacks string into parts using comma
     */
    @Override
    public List<String> valueOf(String value)
    {
        return Arrays.asList(value.split(","));
    }
}
