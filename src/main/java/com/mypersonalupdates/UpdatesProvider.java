package com.mypersonalupdates;

import java.util.ArrayList;

public interface UpdatesProvider {
    Integer getID();
    ArrayList<String> getAcceptedFilterFields();
    Integer subscribe (Filter filter, UpdatesConsumer cosumer);
    void unsubscribe (Integer subscriberID);
}
