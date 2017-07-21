package com.mypersonalupdates;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.Date;

public interface Update {
    UpdatesProvider getProvider() throws DBException;
    Date getTimestamp() throws DBException;
    Collection<String> getAttributeValues(UpdatesProviderAttribute attr) throws DBException;
    String getIDFromProvider() throws DBException;
}
