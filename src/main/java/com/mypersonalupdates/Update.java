package com.mypersonalupdates;

import java.util.ArrayList;
import java.util.Date;

public interface Update {

    String getProviderID();
    String getSource();
    String getText();
    String getID();
    ArrayList<String> getLinks();
    ArrayList<String> getMultimedia();
    ArrayList<String> getHashtags();
    ArrayList<String> getMentions();
    Date getTimestamps();
    Integer getLikesCount();
    Integer getSharedCount();
    boolean isOwnStatus();
    void getOriginalStatus();   //TODO: modificar el tipo
}
