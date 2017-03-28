package com.mypersonalupdates.webserver.requests;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mypersonalupdates.webserver.session.Session;
import spark.Response;

public class SparkRequestWrapper implements Request {
    private static String SESSION_COOKIE_NAME = "MyPersonalUpdatesSessID";
    private static long SESSION_LIFETIME = 259200; // 3 days

    private Session session = null;
    private spark.Request request;
    private Response response;

    public SparkRequestWrapper(spark.Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Session getSession() {
        // Si no tengo sesión en caché
        if(this.session == null) {
            // Obtengo el id de sesión informado por el usuario
            String sessid = this.request.cookie(SparkRequestWrapper.SESSION_COOKIE_NAME);

            // Si el usuario informó una id
            if (sessid != null) {
                // Recupero los datos de la sesión
                this.session = Session.getFromID(sessid);
            }
        }

        // Me fijo si la sesión en la caché está vencida
        if(this.session != null && !this.session.isNew() && this.session.getSecondsFromLastSave() > SparkRequestWrapper.SESSION_LIFETIME) {
            // Si está vencida, la borro
            Session.destroy(this.session);
            this.session = null;
        }

        // Si no tengo sesión en la caché
        if(this.session == null) {
            // Creo una nueva
            this.session = new Session();

            // Le informo al usuario la nueva id de sesión
            this.response.cookie(SparkRequestWrapper.SESSION_COOKIE_NAME, this.session.getID());
        }

        // Devuelvo la sesión cacheada
        return this.session;
    }

    @Override
    public String getRawData() {
        return this.request.body();
    }

    @Override
    public <T> T getData(Class<T> tClass) throws InvalidRequestBodyExeption {
        Gson gson = new Gson();

        T data;

        try {
            data = gson.fromJson(this.request.body(), tClass);
        }catch (JsonSyntaxException e) {
            throw new InvalidRequestBodyExeption();
        }

        return data;
    }

    @Override
    public void close() {
        if(this.session != null && this.session.isModified())
            this.session.save();
    }
}
