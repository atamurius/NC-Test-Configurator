package com.netcracker.sova.types.map;

import java.util.Map;

import com.netcracker.sova.types.SimpleTypeReader;

public class MapTypeReader extends SimpleTypeReader
{
    public MapTypeReader()
    {
        super(new MapType(), Map.class);
    }
}
