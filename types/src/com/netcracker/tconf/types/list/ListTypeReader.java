package com.netcracker.tconf.types.list;

import com.netcracker.tconf.types.SimpleTypeReader;

public class ListTypeReader extends SimpleTypeReader
{
    public ListTypeReader()
    {
        super(new ListType(), java.util.List.class);
    }
}
