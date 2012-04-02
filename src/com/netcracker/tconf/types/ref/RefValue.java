package com.netcracker.tconf.types.ref;

import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tconf.io.DataException;
import com.netcracker.tconf.io.xml.ValueReader;
import com.netcracker.tconf.io.xml.ValueWriter;
import com.netcracker.tconf.model.Output;
import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Scenario;
import com.netcracker.tconf.model.Type;
import com.netcracker.util.xml.Constructor;

public class RefValue implements ValueReader, ValueWriter
{
    @Override
    public boolean readValue(Parameter prop, Element elem,
            Map<String, Scenario> scenarios) throws DataException
    {
        if (prop.getType() instanceof RefType) {
            if (elem.getAttribute("value").isEmpty()) {
                prop.setValue(null);
                return true;
            }
            String[] parts = elem.getAttribute("value").split("\\.");
            prop.setValue(scenarios.get(parts[0]).outputs().get(parts[1]));
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
            if (value == null)
                xml.attr("value", "");
            else
                xml.attr("value", "scenario"+ res.getScenario().getIndex() + "." + res.getName());
            return true;
        }
        else
            return false;
    }

}
