package com.netcracker.tconf.types.map;

import java.util.Map;

import com.netcracker.tconf.types.SimpleTypeReader;

public class MapTypeReader extends SimpleTypeReader
{
    public MapTypeReader()
    {
        super(new MapType(), Map.class);
    }
}
