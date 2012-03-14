package com.netcracker.tc.configurator.data.xml;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.TestGroupWriter;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Result;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
import com.netcracker.tc.model.TestGroup;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.model.types.RefType;
import com.netcracker.xml.Constructor;

public class XmlTestGroupWriter implements TestGroupWriter
{
    private final String filename;
    
    public XmlTestGroupWriter(String filename)
    {
        this.filename = filename;
    }
    
    @Override
    public void write(TestGroup testGroup) throws DataException
    {
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
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
            finally {
                out.close();
            }
        }
        catch (Exception e) {
            throw new DataException(e);
        }
    }
    
    private void writeValue(Constructor xml, Type type, Object value)
    {
        if (type instanceof RefType) {
            Result res = (Result) value;
            xml.attr("value", "scenario"+ res.getScenario().getIndex() + "." +res.getName());
        }
        else
            xml.attr("value", value.toString());
    }
}
