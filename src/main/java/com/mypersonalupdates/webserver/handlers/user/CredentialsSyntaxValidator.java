package com.mypersonalupdates.webserver.handlers.user;

import com.google.gson.JsonElement;
import com.mypersonalupdates.Config;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.webserver.Request;
import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

import java.util.regex.Pattern;

/**
 * Esta clase se encarga de procesar una parte de una petición
 * que necesita de las credenciales de inicio de sesión de un
 * usuario. Particularmente se encarga de chequear que la
 * sintaxis de las credenciales sea la correcta.
 * @param <T> Tipo de respuesta esperado al finalizar el procesamiento de la petición
 */
public final class CredentialsSyntaxValidator<T> extends Handler<T> {
    private static final Pattern REGEX_USERNAME = Pattern.compile(Config.get().getString("regex.username"));
    private static final Pattern REGEX_PASSWORD = Pattern.compile(Config.get().getString("regex.password"));

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private static final Response INVALID_DATA_RESPONSE = new Response.Builder(Handler.BAD_REQUEST_RESPONSE)
            .setType("InvalidUserCredentials")
            .setMessage("Las credenciales de usuario no tienen el formato correcto.")
            .build();

    private String getData(Request request, String fieldName) {
        return (String) request.getData(fieldName, String.class);
    }

    public String getUsername(Request request) {
        return this.getData(request, USERNAME_KEY);
    }

    public String getPassword(Request request) {
        return this.getData(request, PASSWORD_KEY);
    }

    @Override
    public void processRequest(Request request, Response response) throws DBException, SealedException, RequestProcessingException {
        if(this.getUsername(request) != null && this.getPassword(request) != null)
            return;

        JsonElement userElement = null, passElement = null;

        if(request.hasBody()) {
            userElement = request.getBody().get(USERNAME_KEY);
            passElement = request.getBody().get(PASSWORD_KEY);
        }

        if(userElement == null || passElement == null)
            throw new RequestProcessingException(response.set(Handler.BAD_REQUEST_RESPONSE));

        String username, password;
        try {
            username = userElement.getAsString();
            password = passElement.getAsString();
        } catch (ClassCastException e) {
            throw new RequestProcessingException(response.set(INVALID_DATA_RESPONSE), e);
        }

        boolean userOK = REGEX_USERNAME.matcher(username).matches();
        boolean passOK = REGEX_PASSWORD.matcher(password).matches();

        if (!userOK || !passOK)
            throw new RequestProcessingException(response.set(INVALID_DATA_RESPONSE));
        else
            request .setData(USERNAME_KEY, username)
                    .setData(PASSWORD_KEY, password);
    }
}
