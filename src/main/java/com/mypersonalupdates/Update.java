package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface Update {

    String getProviderID();
    String getSource();
    String getText();
    String getID();
    Date getTimestamps();

    List<String> getLinks();
    List<String> getMultimedia();
    List<String> getHashtags();
    List<String> getMentions();
    Integer getLikesCount();
    Integer getSharedCount();
    boolean isOwnUpdate();
    Update getOriginalUpdate();

    Collection<String> getAttributeValues(UpdatesProviderAttribute attr);
}
