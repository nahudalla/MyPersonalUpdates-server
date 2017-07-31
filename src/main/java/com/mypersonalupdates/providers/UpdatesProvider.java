package com.mypersonalupdates.providers;

import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.providers.ProviderRequestProcessor;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.realtime.UpdatesConsumer;
import com.mypersonalupdates.users.User;

import java.util.Collection;
import java.util.Map;

/**
 * Esta interfaz representa a un proveedor de actualizaciones.
 */
public interface UpdatesProvider {
    // TODO: actualizar diagrama
    Long getID();
    String getName();
    String getDescription();
    Collection<UpdatesProviderAttribute> getAttributes();
    Long subscribe(User user, Filter filter, UpdatesConsumer consumer) throws UserNotLoggedInToProviderException;
    boolean unsubscribe(User user, Long subscriberID);
    void stop();
    @Override
    boolean equals(Object o);
    Map<String, ProviderRequestProcessor> getActions();
    @Override
    int hashCode();
}
