package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface Update {

    // TODO: arreglar nombre
    Date getTimestamps();
    Collection<String> getAttributeValues(UpdatesProviderAttribute attr);

    // TODO: agregar: getProvider
}
