package com.mypersonalupdates.webserver.services;

import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.requests.InvalidRequestBodyExeption;
import com.mypersonalupdates.webserver.requests.Request;
import com.mypersonalupdates.webserver.requests.LoginRequest;
import com.mypersonalupdates.webserver.responses.Message;

// Handles "/login" service
public class LoginService implements Service{
    private static String USERID_SESSION_FIELD_NAME = "userid";

    public static boolean isUserAuthenticated(Request request) {
        return request.getSession().get(LoginService.USERID_SESSION_FIELD_NAME) != null;
    }

    public static void removeUserAuthenthication(Request request) {
        request.getSession().remove(LoginService.USERID_SESSION_FIELD_NAME);
    }

    @Override
    public Object handle(Request request) throws Exception {
        if(LoginService.isUserAuthenticated(request))
            return new Message("login-ok");

        LoginRequest loginRequest = request.getData(LoginRequest.class);

        if(loginRequest == null || loginRequest.getUser() == null || loginRequest.getPassword() == null)
            throw new InvalidRequestBodyExeption("Not all required fields were received.");

        User user = User.getFromCredentials(loginRequest.getUser(), loginRequest.getPassword());

        if(user == null)
            return new Message("login-invalid-credentials");

        request.getSession().set(LoginService.USERID_SESSION_FIELD_NAME, user.getID().toString());

        return new Message("login-ok");
    }
}
