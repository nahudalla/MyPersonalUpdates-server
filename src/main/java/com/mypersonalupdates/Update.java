package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.Date;

public interface Update {

    // TODO: arreglar nombre
    Date getTimestamps();
    Collection<String> getAttributeValues(UpdatesProviderAttribute attr);
    UpdatesProvider getProvider();
}
