package com.mypersonalupdates.exceptions;


public final class UserNotLoggedInToProviderException extends Exception {
    public UserNotLoggedInToProviderException() {
        super("El usuario no se encuentra logueado en el proveedor.");
    }

    public UserNotLoggedInToProviderException(String message) {
        super(message);
    }

    public UserNotLoggedInToProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotLoggedInToProviderException(Throwable cause) {
        super(cause);
    }

    public UserNotLoggedInToProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
