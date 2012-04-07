package com.netcracker.sova.types.list;

import com.netcracker.sova.types.SimpleTypeReader;

public class ListTypeReader extends SimpleTypeReader
{
    public ListTypeReader()
    {
        super(new ListType(), java.util.List.class);
    }
}
