package com.netcracker.tc.configurator.data;

import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.TestGroup;

public interface TestGroupReader
{
    void read(TestGroup testGroup, ActionGroup actions) throws DataException;
}
