package com.mypersonalupdates.webserver.services;

import com.mypersonalupdates.webserver.requests.Request;

public interface Service {
    Object handle(Request request) throws Exception;
}
