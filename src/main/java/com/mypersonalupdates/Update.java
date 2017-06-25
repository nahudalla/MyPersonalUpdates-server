package com.mypersonalupdates;

import java.util.ArrayList;
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
}
