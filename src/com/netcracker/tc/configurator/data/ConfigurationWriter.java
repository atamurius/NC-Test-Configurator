package com.netcracker.tc.configurator.data;

import java.io.OutputStream;

import com.netcracker.tc.model.Configuration;

/**
 * Abstract test configuration storage.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 *
 */
public interface ConfigurationWriter
{
    /**
     * Writes tests into storage.
     * @param out output stream to store data
     * @param conf test configuration to store
     * @throws DataException if something goes wrong
     */
    void write(OutputStream out, Configuration conf) throws DataException;
}
