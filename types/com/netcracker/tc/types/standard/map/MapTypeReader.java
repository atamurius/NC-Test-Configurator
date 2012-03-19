package com.netcracker.tc.types.standard.map;

import java.util.Map;

import com.netcracker.tc.types.standard.SimpleTypeReader;

public class MapTypeReader extends SimpleTypeReader
{
    public MapTypeReader()
    {
        super(new MapType(), Map.class);
    }
}
