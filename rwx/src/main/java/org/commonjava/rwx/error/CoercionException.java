package org.commonjava.rwx.error;

public class CoercionException
                extends XmlRpcException
{

    private static final long serialVersionUID = 1L;

    public CoercionException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    public CoercionException( final String message )
    {
        super( message );
    }

}
