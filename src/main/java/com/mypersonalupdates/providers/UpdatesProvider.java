package com.mypersonalupdates.providers;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.UpdatesConsumer;
import com.mypersonalupdates.users.User;

import java.util.List;

public interface UpdatesProvider {
    Integer getID();
    List<String> getAcceptedFilterFields();
    // TODO: update diagram
    Integer subscribe (User user, Filter filter, UpdatesConsumer cosumer);
    // TODO: update diagram (true if the user was subscribed and now is not)
    boolean unsubscribe (User user, Integer subscriberID);
}
