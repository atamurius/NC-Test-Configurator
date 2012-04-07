package com.netcracker.sova.types.bool;

import com.netcracker.sova.types.SimpleTypeReader;

public final class BoolTypeReader extends SimpleTypeReader
{
    public BoolTypeReader()
    {
        super(new BoolType(), Boolean.TYPE, Boolean.class);
    }
}