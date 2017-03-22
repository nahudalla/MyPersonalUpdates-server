package com.mypersonalupdates.webserver.responses;
public class Error extends Message {
    private int code;

    public Error(int code) {
        super("error", "Error "+code);
        this.code = code;
    }

    public Error(int code, String message) {
        super("error", message);
        this.code = code;
    }
}
