package com.netcracker.tc.configurator.data.xml;

import static com.netcracker.xml.Xml.elements;
import static com.netcracker.xml.Xml.load;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.TestGroupReader;
import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
import com.netcracker.tc.model.TestGroup;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.model.types.BoolType;
import com.netcracker.tc.model.types.IntType;
import com.netcracker.tc.model.types.RefType;

public class XmlTestGroupReader implements TestGroupReader
{
    private final String filename;

    public XmlTestGroupReader(String filename)
    {
        this.filename = filename;
    }

    @Override
    public void read(TestGroup testGroup, ActionGroup actions) throws DataException
    {
        try {
            Element testsE = load(filename).getDocumentElement(); 
            
            for (Element testE : elements(testsE, "/tests/test")) {
                Test test = new Test(testE.getAttribute("title"));
                testGroup.add(test);
                Map<String, Scenario> scenarios = new HashMap<String, Scenario>();
                
                for (Element scE : elements(testE, "scenario")) {
                    String id = scE.getAttribute("id");
                    Scenario scenario = new Scenario(actions.find(scE.getAttribute("type")));
                    test.add(scenario);
                    scenario.setTitle(scE.getAttribute("title"));
                    scenarios.put(id, scenario);
                    
                    for (Element propE : elements(scE, "property")) {
                        Property prop = scenario.properties().get(propE.getAttribute("name"));
                        prop.setValue(readValue(prop.getType(), propE, scenarios));
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataException(e);
        }
    }

    private Object readValue(Type type, Element prop, Map<String, Scenario> scenarios)
    {
        if (type instanceof BoolType) {
            return Boolean.valueOf(prop.getAttribute("value"));
        }
        if (type instanceof IntType) {
            return Integer.parseInt(prop.getAttribute("value"));
        }
        if (type instanceof RefType) {
            String[] parts = prop.getAttribute("value").split("\\.");
            return scenarios.get(parts[0]).results().get(parts[1]);
        }
        else
            return prop.getAttribute("value");
    }
}






