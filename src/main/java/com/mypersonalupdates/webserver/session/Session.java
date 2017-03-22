package com.mypersonalupdates.webserver.session;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.Hashtable;
import java.util.Map;

public class Session {
    private static SessionStore sessionStore = new InMemorySessionStore();
    public static void setSessionStore(SessionStore sessionStore) {
        Session.sessionStore = sessionStore;
    }

    private String ID;
    private SessionData sessionData;
    private boolean isModified = false;

    public Session() {
        this.ID = Session.sessionStore.getNewSessionID();
        this.sessionData = new SessionData();
        this.sessionData.data = new Hashtable<>();
        this.sessionData.creationTime = Instant.now().getEpochSecond();
        this.sessionData.lastSaveTime = 0;
    }

    private Session(String id, SessionData sessionData) {
        this.ID = id;
        this.sessionData = sessionData;
    }

    public static Session getFromID(String id) {
        String sessionData = Session.sessionStore.getSessionData(id);

        if(sessionData == null)
            return null;

        Gson gson = new Gson();

        return new Session(id, gson.fromJson(sessionData, SessionData.class));
    }

    public static void destroy(Session session) {
        Session.sessionStore.destroySession(session.getID());
    }

    public void save() {
        Gson gson = new Gson();
        this.sessionData.lastSaveTime = Instant.now().getEpochSecond();
        Session.sessionStore.setSessionData(this.ID, gson.toJson(this.sessionData));
        this.isModified = false;
    }

    public String getID() {
        return this.ID;
    }

    public void set(String key, String value) {
        this.isModified = true;
        this.sessionData.data.put(key, value);
    }

    public String get(String key) {
        return this.sessionData.data.get(key);
    }

    public void remove(String key) {
        this.isModified = true;
        this.sessionData.data.remove(key);
    }

    public long getSecondsFromCreation() {
        return Instant.now().getEpochSecond() - this.sessionData.creationTime;
    }

    public long getSecondsFromLastSave() {
        return Instant.now().getEpochSecond() - this.sessionData.lastSaveTime;
    }

    public boolean isNew() {
        return this.sessionData.lastSaveTime == 0;
    }

    public boolean isModified() {
        return this.isModified;
    }

    private class SessionData {
        public Map<String, String> data;
        public long creationTime;
        public long lastSaveTime;
    }
}
