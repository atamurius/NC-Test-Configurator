package com.netcracker.sova.io.xml;

import java.util.Map;

import org.w3c.dom.Element;

import com.netcracker.sova.io.DataException;
import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Scenario;

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
    boolean readValue(Parameter prop, Element elem, Map<String, Scenario> scenarios) 
            throws DataException;
}