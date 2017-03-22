package com.mypersonalupdates.webserver.requests;

public class InvalidRequestBodyExeption extends Exception {
    public InvalidRequestBodyExeption() {
    }

    public InvalidRequestBodyExeption(String message) {
        super(message);
    }

    public InvalidRequestBodyExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestBodyExeption(Throwable cause) {
        super(cause);
    }

    public InvalidRequestBodyExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
