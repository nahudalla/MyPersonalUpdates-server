package com.mypersonalupdates.webserver.responses;

public class Message {
    private String type = null;
    private String message = null;

    public Message(String type) {
        this.type = type;
    }

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }
}
