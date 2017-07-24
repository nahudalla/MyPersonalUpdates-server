package com.mypersonalupdates.providers;

import com.mypersonalupdates.UpdatesConsumer;

public interface UpdatesProvider {
    Integer getID();
    // TODO: actualizar signature
    Integer subscribe (Filter filter, UpdatesConsumer cosumer);
    // TODO: actualizar signature
    void unsubscribe (Integer subscriberID);

    // TODO: agregar: getName, getDescription, getAttributes, stop, equals
    // TODO: agregar equals a diagrama
}
