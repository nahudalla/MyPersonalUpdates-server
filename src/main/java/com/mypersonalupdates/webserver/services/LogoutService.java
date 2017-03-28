package com.mypersonalupdates.webserver.services;

import com.mypersonalupdates.webserver.requests.Request;
import com.mypersonalupdates.webserver.responses.Message;

public class LogoutService implements Service {
    @Override
    public Object handle(Request request) throws Exception {
        LoginService.removeUserAuthenthication(request);
        return new Message("logout-ok");
    }
}
