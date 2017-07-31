package com.mypersonalupdates.webserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Esta clase representa una petición recibida por el
 * sistema y almacena su información.
 */
public abstract class Request extends ConnectionData {
    private final Map<String, Collection<String>> queryParams, pathParams;

    private final Map<Class<?>, Object> dataByType = new Hashtable<>();
    private final Map<Class<?>, Map<String, Object>> dataByName = new Hashtable<>();

    private boolean canBeEmpty = true;

    protected Request(
            Map<String, ? extends Collection<String>> pathParams,
            Map<String, ? extends Collection<String>> queryParams,
            Map<String, ? extends Collection<String>> headers,
            String JSONBody
    ) throws RequestProcessingException {
        super(headers, JSONBody);

        this.close();

        this.queryParams = queryParams == null ? null : new Hashtable<>(queryParams);
        this.pathParams = pathParams == null ? null : new Hashtable<>(pathParams);
    }

    /* Observadores */

    public boolean hasQueryParams() {
        return this.queryParams != null && !this.queryParams.isEmpty();
    }

    public boolean hasPathParams() {
        return this.pathParams != null &&  !this.pathParams.isEmpty();
    }

    public boolean canBeEmpty() {
        return this.canBeEmpty;
    }

    @Override
    public boolean isEmpty() {
        // Los headers no participan para saber si una petición esta vacía
        return !this.canBeEmpty && super.isEmpty() && !this.hasPathParams() && !this.hasQueryParams();
    }

    /* Getters */

    @Override
    public Map<String, Collection<String>> getHeaders() {
        return super.getHeaders();
    }

    @Override
    public Collection<String> getHeader(String name) {
        return super.getHeader(name);
    }

    @Override
    public JsonObject getBody() {
        return super.getBody();
    }

    @Override
    public JsonElement getBodyItem(String name) {
        return super.getBodyItem(name);
    }

    @Override
    public <T> T getBodyItem(String name, Class<T> tClass) throws RequestProcessingException {
        return super.getBodyItem(name, tClass);
    }

    @Override
    public <T> T getBodyItem(String name, Type type) throws RequestProcessingException {
        return super.getBodyItem(name, type);
    }

    public Collection<String> getQueryParamValues(String name) {
        return this.queryParams == null ? null : this.queryParams.get(name);
    }

    public Collection<String> getPathParamValues(String name) {
        return this.pathParams == null ? null : this.pathParams.get(name);
    }

    public String getQueryParam(String name) {
        return getValueFrom(name, this.queryParams);
    }

    public String getPathParam(String name) {
        return getValueFrom(name, this.pathParams);
    }

    public Object getData(String name, Class<?> tClass) {
        Map<String, Object> map = this.dataByName.get(tClass);

        return map == null ? null : map.get(name);
    }

    public Object getData(Class<?> tClass) {
        return this.dataByType.get(tClass);
    }

    /* Setters */

    public Request setData(String name, Object data) {
        return this.setData(name, data, data.getClass());
    }

    public Request setData(String name, Object data, Class<?> tClass) {
        Map<String, Object> map = this.dataByName.get(tClass);

        if(map == null) {
            map = new Hashtable<>();
            map.put(name, data);
            this.dataByName.put(tClass, map);
        } else
            map.put(name, data);

        return this;
    }

    public Request setData(Object data) {
        return this.setData(data, data.getClass());
    }

    public Request setData(Object data, Class<?> tClass) {
        this.dataByType.put(tClass, data);

        return this;
    }

    public Request setCanBeEmpty(boolean canBeEmpty) {
        this.canBeEmpty = canBeEmpty;
        return this;
    }

    /* Modificadores */

    public Object removeData(Class<?> tClass) {
        return this.dataByType.remove(tClass);
    }

    public Object removeData(String name, Class<?> tClass) {
        Map<String, Object> map = this.dataByName.get(tClass);

        if(map == null)
            return null;

        Object o = map.remove(name);

        if(map.size() == 0)
            this.dataByName.remove(tClass);

        return o;
    }

    /* Otros */

    private static String getValueFrom(String name, Map<String, Collection<String>> dataSource) {
        Collection<String> values = dataSource == null ? null : dataSource.get(name);
        Iterator<String> it;

        return (values != null && (it = values.iterator()).hasNext()) ? it.next() : null;
    }


}
