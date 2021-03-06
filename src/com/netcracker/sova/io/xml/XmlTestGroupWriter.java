package com.netcracker.sova.io.xml;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.netcracker.sova.io.ConfigurationWriter;
import com.netcracker.sova.io.DataException;
import com.netcracker.sova.model.Configuration;
import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Scenario;
import com.netcracker.sova.model.Test;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.types.ref.RefValue;
import com.netcracker.util.ClassRegistry;
import com.netcracker.util.xml.Constructor;

public class XmlTestGroupWriter implements ConfigurationWriter, ClassRegistry
{
    private final List<ValueWriter> writers = new ArrayList<ValueWriter>();

    public void register(Class<?> type)
    {
        try {
            if (ValueWriter.class.isAssignableFrom(type))
                writers.add(type.asSubclass(ValueWriter.class).newInstance());
        }
        catch (Exception e) {
            System.out.println("XmlTestGroupWriter: Failed to load "+ type);
        }
    }
    {
        // register default types
        register(RefValue.class);
    }
    
    @Override
    public void write(OutputStream out, Configuration testGroup) throws DataException
    {
        try {
            Constructor xml = new Constructor().node("tests");
            for (Test test : testGroup.tests()) {
                xml.node("test").attr("title", test.getTitle());
                int index = 0;
                for (Scenario scenario : test.scenarios()) {
                    xml.node("scenario")
                       .attr("id","scenario"+ (index++))
                       .attr("title", scenario.getTitle())
                       .attr("type", scenario.getSchema().getName());
                    for (Parameter prop : scenario.parameters().values()) {
                        xml.node("property")
                           .attr("name", prop.getName());
                        writeValue(xml, prop.getType(), prop.getValue());
                        xml.end();
                    }
                    xml.end();
                }
                xml.end();
            }
            xml.writeTo("tests.dtd", out);
        }
        catch (Exception e) {
            throw new DataException(e);
        }
    }
    
    private void writeValue(Constructor xml, Type type, Object value)
    {
        for (ValueWriter writer : writers)
            if (writer.writeValue(xml, type, value))
                return;
        // default:
        xml.attr("value", value.toString());
    }
}
