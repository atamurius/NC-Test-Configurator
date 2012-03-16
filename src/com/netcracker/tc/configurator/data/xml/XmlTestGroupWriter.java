package com.netcracker.tc.configurator.data.xml;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.TestGroupWriter;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
import com.netcracker.tc.model.TestGroup;
import com.netcracker.tc.model.Type;
import com.netcracker.xml.Constructor;

public class XmlTestGroupWriter implements TestGroupWriter
{
    private final List<ValueWriter> writers = new ArrayList<ValueWriter>();

    public void register(Class<?> type) throws InstantiationException, IllegalAccessException
    {
        if (ValueWriter.class.isAssignableFrom(type))
            writers.add(type.asSubclass(ValueWriter.class).newInstance());
    }
    
    @Override
    public void write(OutputStream out, TestGroup testGroup) throws DataException
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
                       .attr("type", scenario.getPrototype().getName());
                    for (Property prop : scenario.properties().values()) {
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
