package com.netcracker.tc.configurator.data;

import com.netcracker.tc.model.TestGroup;

public interface TestGroupWriter
{
    void write(TestGroup testGroup) throws DataException;
}
