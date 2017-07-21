package com.mypersonalupdates.providers;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.UpdatesConsumer;
import com.mypersonalupdates.users.User;

import java.util.Collection;

public interface UpdatesProvider {
    // TODO: actualizar diagrama
    Integer getID();
    String getName();
    String getDescription();
    Collection<UpdatesProviderAttribute> getAttributes();
    Long subscribe(User user, Filter filter, UpdatesConsumer consumer);
    boolean unsubscribe(User user, Long subscriberID);
    void stop();
}
