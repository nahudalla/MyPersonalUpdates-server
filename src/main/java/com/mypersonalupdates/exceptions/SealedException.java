package com.mypersonalupdates.exceptions;

/**
 * Esta clase es una excepción que representa que
 * se intentó modificar un objeto que ha sido cerrado
 * (o sellado) previamente, es decir que se marcó de
 * algún modo como que no puede ser modificado.
 */
public final class SealedException extends Exception {
    public SealedException() {
        super("Attempting to modify a sealed object.");
    }

    public SealedException(String message) {
        super(message);
    }

    public SealedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SealedException(Throwable cause) {
        super(cause);
    }

    public SealedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
