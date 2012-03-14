package com.netcracker.tc.configurator.data;

import com.netcracker.tc.model.ActionGroup;

public interface ActionReader
{
    ActionGroup readActionGroup() throws DataException;
}
