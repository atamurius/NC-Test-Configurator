package com.netcracker.tc.configurator.data;

import java.io.OutputStream;

import com.netcracker.tc.model.TestGroup;

public interface TestGroupWriter
{
    void write(OutputStream out, TestGroup testGroup) throws DataException;
}
