package com.netcracker.tc.configurator.data;

import java.io.InputStream;

import com.netcracker.tc.model.Schemas;
import com.netcracker.tc.model.Configuration;

/**
 * Abstract configuration source.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public interface ConfigurationReader
{
    /**
     * Reads configuration into conf, using given schemas.
     * @param in input stream with data
     * @param conf configuration to add tests from source 
     * @param schemas current schemas
     * @throws DataException if can't read data from source
     */
    void read(InputStream in, Configuration conf, Schemas schemas) throws DataException;
}
