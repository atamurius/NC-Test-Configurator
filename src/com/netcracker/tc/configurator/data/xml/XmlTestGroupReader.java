package com.netcracker.tc.configurator.data.xml;

import static com.netcracker.xml.Xml.elements;
import static com.netcracker.xml.Xml.load;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.TestGroupReader;
import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
import com.netcracker.tc.model.TestGroup;

public class XmlTestGroupReader implements TestGroupReader
{
    private final List<ValueReader> readers = new ArrayList<ValueReader>();

    public void register(Class<?> type) throws InstantiationException, IllegalAccessException
    {
        if (ValueReader.class.isAssignableFrom(type))
            readers.add(type.asSubclass(ValueReader.class).newInstance());
    }

    @Override
    public void read(InputStream in, TestGroup testGroup, ActionGroup actions) throws DataException
    {
        try {
            Element testsE = load(in).getDocumentElement(); 
            
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
                        readValue(prop, propE, scenarios);
                    }
                }
            }
        }
        catch (DataException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataException(e);
        }
    }

    private void readValue(Property prop, Element el, Map<String, Scenario> scenarios) throws DataException
    {
        for (ValueReader reader : readers) {
            if (reader.readValue(prop, el, scenarios))
                return;
        }
        // default:
        prop.setValue(prop.getType().valueOf(el.getAttribute("value")));
    }
}






