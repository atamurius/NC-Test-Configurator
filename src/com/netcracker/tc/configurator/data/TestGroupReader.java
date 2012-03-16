package com.netcracker.tc.configurator.data;

import java.io.InputStream;

import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.TestGroup;

public interface TestGroupReader
{
    void read(InputStream in, TestGroup testGroup, ActionGroup actions) throws DataException;
}
