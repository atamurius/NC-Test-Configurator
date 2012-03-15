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

import com.netcracker.tc.configurator.data.SchemaReader;
import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.model.Action;
import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Result;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.BoolType;
import com.netcracker.tc.types.standard.EnumType;
import com.netcracker.tc.types.standard.IntType;
import com.netcracker.tc.types.standard.RefType;
import com.netcracker.tc.types.standard.SetType;
import com.netcracker.tc.types.standard.StringType;

public class XmlActionReader implements SchemaReader
{
    private final Document doc;

    public XmlActionReader(String filename) throws DataException
    {
        try {
            doc = load(filename);
        }
        catch (SAXException e) {
            throw new DataException(e);
        }
        catch (IOException e) {
            throw new DataException(e);
        }
        catch (ParserConfigurationException e) {
            throw new DataException(e);
        }
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
                return new IntType(Integer.MIN_VALUE, Integer.parseInt(max));
            if (max.isEmpty())
                return new IntType(Integer.parseInt(min), Integer.MAX_VALUE);
            else
                return new IntType(Integer.parseInt(min), Integer.parseInt(max));
        }
        if ("ref".equals(type)) {
            return new RefType(
                    property.getAttribute("ref-type"), 
                    ! property.getAttribute("required").equals("false"));
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
                return new StringType(false, 0, patt);
            else
                return new StringType(
                        req.equalsIgnoreCase("true") || req.equalsIgnoreCase("yes"), 
                        maxl.isEmpty() ? 0 : Integer.parseInt(maxl), "");
        }
        else
            throw new DataException("Unknown property type: "+ type);
    }
}
