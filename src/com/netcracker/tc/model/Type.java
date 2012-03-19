package com.netcracker.tc.model;

/**
 * Parameter type with restrictions.
 * Should be constant value,
 * only getters for parameters are allowed.
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
    String validate(Parameter property);
    
    /**
     * Default type value.
     * @return default value
     */
    Object defaultValue();
    
    /**
     * Convert string to value of this type.
     * Used for scenario default value reading.
     * @param value
     * @return value of current type
     */
    Object valueOf(String value);
}
