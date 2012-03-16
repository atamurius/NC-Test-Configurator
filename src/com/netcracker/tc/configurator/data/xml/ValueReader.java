package com.netcracker.tc.configurator.data.xml;

import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Scenario;

/**
 * Xml tests loader module.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public interface ValueReader
{
    /**
     * Tries to read property value from xml property element,
     * if reading was successfull method must set property value.
     * @param prop property to set value
     * @param elem property xml element
     * @param scenarios previously read scenarios with ids
     * @return true if reading was successfull
     * @throws DataException if value has wrong format
     */
    boolean readValue(Property prop, Element elem, Map<String, Scenario> scenarios) 
            throws DataException;
}