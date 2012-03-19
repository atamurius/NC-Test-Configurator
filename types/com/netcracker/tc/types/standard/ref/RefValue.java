package com.netcracker.tc.types.standard.ref;

import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.xml.ValueReader;
import com.netcracker.tc.configurator.data.xml.ValueWriter;
import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Output;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Type;
import com.netcracker.xml.Constructor;

public class RefValue implements ValueReader, ValueWriter
{
    @Override
    public boolean readValue(Parameter prop, Element elem,
            Map<String, Scenario> scenarios) throws DataException
    {
        if (prop.getType() instanceof RefType) {
            String[] parts = elem.getAttribute("value").split("\\.");
            prop.setValue(scenarios.get(parts[0]).results().get(parts[1]));
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean writeValue(Constructor xml, Type type, Object value)
    {
        if (type instanceof RefType) {
            Output res = (Output) value;
            xml.attr("value", "scenario"+ res.getScenario().getIndex() + "." + res.getName());
            return true;
        }
        else
            return false;
    }

}
