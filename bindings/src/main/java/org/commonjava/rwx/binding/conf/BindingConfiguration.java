package org.commonjava.rwx.binding.conf;

/**
 * Created by jdcasey on 9/16/16.
 */
public class BindingConfiguration
{
    private boolean skipNulls;

    public BindingConfiguration withSkipNulls( boolean skipNulls) {
      this.skipNulls = skipNulls;
      return this;
    }

    public boolean isSkipNulls()
    {
        return skipNulls;
    }
}
