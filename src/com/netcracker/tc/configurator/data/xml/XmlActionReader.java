package com.netcracker.tc.configurator.data.xml;

import static com.netcracker.xml.Xml.elements;
import static com.netcracker.xml.Xml.load;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.netcracker.tc.configurator.data.ActionReader;
import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.model.Action;
import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Result;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.model.types.BoolType;
import com.netcracker.tc.model.types.EnumType;
import com.netcracker.tc.model.types.IntType;
import com.netcracker.tc.model.types.RefType;
import com.netcracker.tc.model.types.SetType;
import com.netcracker.tc.model.types.StringType;

public class XmlActionReader implements ActionReader
{
    private final Document doc;

    public XmlActionReader(String filename) throws SAXException, IOException, ParserConfigurationException
    {
        doc = load(filename);
    }
    
    @Override
    public ActionGroup readActionGroup() throws DataException
    {
        ActionGroup ag = new ActionGroup();
        
        for (Element group : elements(doc, "/schema/group")) {
            String groupTitle = group.getAttribute("title"); 
            
            for (Element action : elements(group, "action")) {
                String actionTitle = action.getAttribute("title");
                List<Property> props = new ArrayList<Property>();

                for (Element prop : elements(action, "property")) {
                    String typeName = prop.getAttribute("type");
                    props.add(new Property(
                            prop.getAttribute("name"), 
                            prop.getAttribute("title"), 
                            prop.getAttribute("description"), 
                            readType(typeName, prop)));
                }
                
                List<Result> res = new ArrayList<Result>();
                
                for (Element result : elements(action, "result")) {
                    res.add(new Result(
                            result.getAttribute("name"), 
                            result.getAttribute("title"), 
                            result.getAttribute("type")));
                }
                String id = action.getAttribute("id");
                ag.add(groupTitle, new Action(id, actionTitle, props, res));
            }
        }
        return ag;
    }

    private Type readType(String type, Element property) throws DataException
    {
        if ("bool".equals(type)) {
            return new BoolType();
        }
        if ("enum".equals(type)) { 
            String[] values = property.getAttribute("values").split(",");
            String def = property.getAttribute("default");
            if (def.isEmpty())
                def = values[0];
            return new EnumType(def, (Object[]) values);
        }
        if ("int".equals(type)) {
            String min = property.getAttribute("min");
            String max = property.getAttribute("max");
            if (min.isEmpty() && max.isEmpty())
                return new IntType();
            if (min.isEmpty())
                return IntType.maxBy(Integer.parseInt(max));
            if (max.isEmpty())
                return IntType.minBy(Integer.parseInt(min));
            else
                return new IntType(Integer.parseInt(min), Integer.parseInt(max));
        }
        if ("ref".equals(type)) {
            return new RefType(property.getAttribute("ref-type"));
        }
        if ("set".equals(type)) {
            String[] values = property.getAttribute("values").split(",");
            return new SetType((Object[]) values);
        }
        if ("string".equals(type)) {
        String maxl = property.getAttribute("max");
            String req = property.getAttribute("required");
            String patt = property.getAttribute("pattern");
            if (! patt.isEmpty())
                return new StringType(patt);
            else
                return new StringType(
                        req.equalsIgnoreCase("true") || req.equalsIgnoreCase("yes"), 
                        maxl.isEmpty() ? 0 : Integer.parseInt(maxl));
        }
        else
            throw new DataException("Unknown property type: "+ type);
    }
}
