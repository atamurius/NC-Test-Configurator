package com.netcracker.tconf.types.bool;

import com.netcracker.tconf.types.SimpleTypeReader;

public final class BoolTypeReader extends SimpleTypeReader
{
    public BoolTypeReader()
    {
        super(new BoolType(), Boolean.TYPE, Boolean.class);
    }
}