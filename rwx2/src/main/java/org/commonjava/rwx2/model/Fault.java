package org.commonjava.rwx2.model;

/**
 * XML-RPC faults are a type of responses. If there was a problem in processing a XML-RPC request, the methodResponse
 * element will contain a fault element instead of a params element. The fault element, like the params element, has
 * only a single value that indicates something went wrong.
 *
 * Created by ruhan on 7/13/17.
 */
public final class Fault
{
    private Object value;

    public Object getValue()
    {
        return value;
    }

    public void setValue( Object value )
    {
        this.value = value;
    }

}
