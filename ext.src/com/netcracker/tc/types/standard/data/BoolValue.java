package com.netcracker.tc.types.standard.data;

import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.xml.ValueReader;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.types.standard.BoolType;

public class BoolValue implements ValueReader
{
    @Override
    public boolean readValue(Property prop, Element elem,
            Map<String, Scenario> scenarios) throws DataException
    {
        if (prop.getType() instanceof BoolType) {
            prop.setValue(Boolean.valueOf(elem.getAttribute("value")));
            return true;
        }
        else
            return false;
    }

}
