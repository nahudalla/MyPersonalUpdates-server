package com.mypersonalupdates.webserver.handlers;

import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;

/**
 * Esta interfaz representa un procesador que se
 * encarga de transformar una respuesta (interna)
 * de una petición, a una apropiada para el medio
 * por el cual se recibió la misma.
 * @param <T> Tipo de la respuesta apropiada al medio de la petición
 */
public interface ResponseProcessor<T> {
    void processBefore(Response response) throws SealedException;
    T processAfter(Response response);
}
