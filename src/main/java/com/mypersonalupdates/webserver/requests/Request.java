package com.mypersonalupdates.webserver.requests;

import com.mypersonalupdates.webserver.session.Session;

public interface Request {
    Session getSession();
    String getRawData();
    <T> T getData(Class<T> tClass) throws InvalidRequestBodyExeption;

    void close();
}
