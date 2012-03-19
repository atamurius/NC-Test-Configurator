package com.netcracker.tc.types.standard.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.xml.ValueReader;
import com.netcracker.tc.configurator.data.xml.ValueWriter;
import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Type;
import com.netcracker.xml.Constructor;
import com.netcracker.xml.Xml;

public class ListValue implements ValueWriter, ValueReader
{
    @Override
    public boolean readValue(Parameter prop, Element elem,
            Map<String, Scenario> scenarios) throws DataException
    {
        if (prop.getType() instanceof ListType) {

            List<String> items = new ArrayList<String>();
        
            for (Element el : Xml.elements(elem, "item"))
                items.add(el.getAttribute("value"));
                    
            prop.setValue(items);
            
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean writeValue(Constructor xml, Type type, Object value)
    {
        if (type instanceof ListType) {
            
            for (Object obj : (List<?>) value)
                xml.node("item").attr("value", obj.toString()).end();
                    
            return true;
        }
        else
            return false;
    }

}
