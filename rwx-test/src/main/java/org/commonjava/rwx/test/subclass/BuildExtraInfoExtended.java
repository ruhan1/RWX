package org.commonjava.rwx.test.subclass;

import org.commonjava.rwx.anno.DataKey;
import org.commonjava.rwx.anno.StructPart;

import static org.commonjava.rwx.test.subclass.Constants.IMPORT_INITIATOR;

/**
 * Created by ruhan on 12/19/17.
 */
@StructPart
public class BuildExtraInfoExtended extends BuildExtraInfo
{
    @DataKey( IMPORT_INITIATOR )
    private String importInitiator;

    public String getImportInitiator()
    {
        return importInitiator;
    }

    public void setImportInitiator( String importInitiator )
    {
        this.importInitiator = importInitiator;
    }
}
