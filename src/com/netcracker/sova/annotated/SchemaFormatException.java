package com.netcracker.sova.annotated;

public class SchemaFormatException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public SchemaFormatException()
    {
    }

    public SchemaFormatException(String arg0)
    {
        super(arg0);
    }

    public SchemaFormatException(Throwable arg0)
    {
        super(arg0);
    }

    public SchemaFormatException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }
}
