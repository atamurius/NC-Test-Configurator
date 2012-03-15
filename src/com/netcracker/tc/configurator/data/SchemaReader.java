package com.netcracker.tc.configurator.data;

import com.netcracker.tc.model.ActionGroup;

public interface SchemaReader
{
    ActionGroup readActionGroup() throws DataException;
}
