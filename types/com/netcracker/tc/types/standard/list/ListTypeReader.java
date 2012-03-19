package com.netcracker.tc.types.standard.list;

import com.netcracker.tc.types.standard.SimpleTypeReader;

public class ListTypeReader extends SimpleTypeReader
{
    public ListTypeReader()
    {
        super(new ListType(), java.util.List.class);
    }
}
