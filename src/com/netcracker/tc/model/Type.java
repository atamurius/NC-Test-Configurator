package com.netcracker.tc.model;

/**
 * Parameter type with restrictions.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public interface Type
{
    /**
     * Checks if value is valid.
     * @param value to check
     * @return null if value is valid, or problem description otherwise
     */
    String validate(Property property);
    
    /**
     * Default type value.
     * @return default value
     */
    Object defaultValue();
}
