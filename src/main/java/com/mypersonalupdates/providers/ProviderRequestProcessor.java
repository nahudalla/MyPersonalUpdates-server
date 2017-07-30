package com.mypersonalupdates.providers;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

/**
 * Esta interfaz representa a un procesador de una
 * acción que un usuario {@link User} puede pedirle
 * a un proveedor {@link UpdatesProvider} que haga.
 */
public interface ProviderRequestProcessor {
    void process(User user, Request request, Response response) throws RequestProcessingException, SealedException, DBException;
}
