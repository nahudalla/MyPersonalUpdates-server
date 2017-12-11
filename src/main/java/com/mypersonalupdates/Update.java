package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.time.Instant;
import java.util.Collection;

/**
 * Esta clase representa una actualización de un proveedor.
 */
public interface Update {
    Long getID();
    UpdatesProvider getProvider();
    Instant getTimestamp();
    Collection<String> getAttributeValues(UpdatesProviderAttribute attr);
    String getIDFromProvider();
}
