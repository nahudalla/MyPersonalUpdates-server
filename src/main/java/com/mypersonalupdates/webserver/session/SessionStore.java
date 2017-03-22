package com.mypersonalupdates.webserver.session;

public interface SessionStore {
    String getNewSessionID();
    void setSessionData(String id, String data);
    String getSessionData(String id);
    void destroySession(String id);
}
