package com.netcracker.tconf.io;

/**
 * Exception while reading or storing test configuration.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 * 
 * @see ConfigurationReader
 * @see ConfigurationWriter
 */
public class DataException extends Exception
{
    private static final long serialVersionUID = 1L;

    public DataException()
    {
        // TODO Auto-generated constructor stub
    }

    public DataException(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public DataException(Throwable arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public DataException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

}
