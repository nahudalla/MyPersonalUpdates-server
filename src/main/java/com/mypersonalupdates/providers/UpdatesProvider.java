package com.mypersonalupdates.providers;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.UpdatesConsumer;

import java.util.ArrayList;
import java.util.List;

public interface UpdatesProvider {
    Integer getID();
    List<String> getAcceptedFilterFields();
    Integer subscribe (Filter filter, UpdatesConsumer cosumer);
    void unsubscribe (Integer subscriberID);
}
