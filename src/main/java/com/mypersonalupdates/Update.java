package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.Date;

public interface Update {
    UpdatesProvider getProvider();
    Date getTimestamp();
    Collection<String> getAttributeValues(UpdatesProviderAttribute attr);
    String getIDFromProvider();
}
