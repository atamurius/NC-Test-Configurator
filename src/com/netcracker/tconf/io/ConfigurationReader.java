package com.netcracker.tconf.io;

import java.io.InputStream;

import com.netcracker.tconf.model.Configuration;
import com.netcracker.tconf.model.Schemas;

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
