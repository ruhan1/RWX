package org.commonjava.rwx2.util;

import org.commonjava.rwx2.error.CoercionException;

public abstract class ValueCoercion
{

    private String description;

    protected ValueCoercion( String description )
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public abstract Object fromString( String value )
        throws CoercionException;

    public String toString( final Object value )
        throws CoercionException
    {
        return value == null ? null : String.valueOf( value );
    }

    public String toString()
    {
        return getDescription();
    }

}
