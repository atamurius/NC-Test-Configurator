package com.netcracker.tc.configurator.data.xml;

import com.netcracker.tc.model.Type;
import com.netcracker.util.xml.Constructor;

/**
 * Xml test writer module.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public interface ValueWriter
{
    /**
     * Writes value of specified type to property xml node.
     * After writing xml constructor must be on same node, as before.
     * 
     * @param xml property node
     * @param type type of value
     * @param value to write
     * @return true if it was known type and value was written
     */
    boolean writeValue(Constructor xml, Type type, Object value);
}