package com.mypersonalupdates.webserver.session;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class InMemorySessionStore implements SessionStore {
    private Map<String, String> sessionsData = new Hashtable<>();

    @Override
    public String getNewSessionID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void setSessionData(String id, String data) {
        this.sessionsData.put(id, data);
    }

    @Override
    public String getSessionData(String id) {
        return this.sessionsData.get(id);
    }

    @Override
    public void destroySession(String id) {
        this.sessionsData.remove(id);
    }
}
