package com.netcracker.tconf.types.map;

import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tconf.io.DataException;
import com.netcracker.tconf.io.xml.ValueReader;
import com.netcracker.tconf.io.xml.ValueWriter;
import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Scenario;
import com.netcracker.tconf.model.Type;
import com.netcracker.util.xml.Constructor;
import com.netcracker.util.xml.Xml;

public class MapValue implements ValueReader, ValueWriter
{

    @SuppressWarnings("unchecked")
    @Override
    public boolean writeValue(Constructor xml, Type type, Object value)
    {
        if (type instanceof MapType) {
            Map<String,String> map = (Map<String, String>) value;
        
            for (String key : map.keySet())
                xml.node("item").
                    attr("key", key).
                    attr("value", map.get(key)).
                    end();
            
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean readValue(Parameter prop, Element elem,
            Map<String, Scenario> scenarios) throws DataException
    {
        if (prop.getType() instanceof MapType) {
            Map<String,String> map = new LinkedHashMap<String, String>();
            
            for (Element e : Xml.elements(elem, "item"))
                map.put(e.getAttribute("key"), e.getAttribute("value"));
                    
            prop.setValue(map);
            
            return true;
        }
        else
            return false;
    }

}
