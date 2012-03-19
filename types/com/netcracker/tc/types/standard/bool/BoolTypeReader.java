package com.netcracker.tc.types.standard.bool;

import com.netcracker.tc.types.standard.SimpleTypeReader;

public final class BoolTypeReader extends SimpleTypeReader
{
    public BoolTypeReader()
    {
        super(new BoolType(), Boolean.TYPE, Boolean.class);
    }
}