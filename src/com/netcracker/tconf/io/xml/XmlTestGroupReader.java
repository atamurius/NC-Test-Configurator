package com.netcracker.tconf.io.xml;

import static com.netcracker.util.xml.Xml.elements;
import static com.netcracker.util.xml.Xml.load;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tconf.io.ConfigurationReader;
import com.netcracker.tconf.io.DataException;
import com.netcracker.tconf.model.Configuration;
import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Scenario;
import com.netcracker.tconf.model.Schemas;
import com.netcracker.tconf.model.Test;
import com.netcracker.util.ClassRegistry;

public class XmlTestGroupReader implements ConfigurationReader, ClassRegistry
{
    private final List<ValueReader> readers = new ArrayList<ValueReader>();

    public void register(Class<?> type)
    {
        try {
            if (ValueReader.class.isAssignableFrom(type))
                readers.add(type.asSubclass(ValueReader.class).newInstance());
            }
        catch (Exception e) {
            System.out.println("XmlTestGroupReader: Failed to load "+ type);
        }
    }

    @Override
    public void read(InputStream in, Configuration testGroup, Schemas actions) throws DataException
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
                        Parameter prop = scenario.parameters().get(propE.getAttribute("name"));
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

    private void readValue(Parameter prop, Element el, Map<String, Scenario> scenarios) throws DataException
    {
        for (ValueReader reader : readers) {
            if (reader.readValue(prop, el, scenarios))
                return;
        }
        // default:
        prop.setValue(prop.getType().valueOf(el.getAttribute("value")));
    }
}






