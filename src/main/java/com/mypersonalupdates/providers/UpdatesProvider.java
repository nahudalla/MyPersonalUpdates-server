package com.mypersonalupdates.providers;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.UpdatesConsumer;

import java.util.ArrayList;
import java.util.List;

public interface UpdatesProvider {
    Integer getID();
    // TODO: actualizar signature
    Integer subscribe (Filter filter, UpdatesConsumer cosumer);
    // TODO: actualizar signature
    void unsubscribe (Integer subscriberID);

    // TODO: agregar: getName, getDescription, getAttributes, stop, equals
    // TODO: agregar equals a diagrama
}
