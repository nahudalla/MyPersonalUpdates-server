package com.mypersonalupdates.webserver.services;

import com.mypersonalupdates.users.User;
import com.mypersonalupdates.webserver.requests.InvalidRequestBodyExeption;
import com.mypersonalupdates.webserver.requests.Request;
import com.mypersonalupdates.webserver.requests.SignupRequest;
import com.mypersonalupdates.webserver.responses.Error;
import com.mypersonalupdates.webserver.responses.Message;

import java.util.regex.Pattern;

public class SignupService implements Service {
    private static Pattern usernamePattern = Pattern.compile("^[a-z][a-z0-9._]{2,29}$");
    private static Pattern passwordLengthPattern = Pattern.compile("^.{6,}$");
    private static Pattern passwordLowercasePattern = Pattern.compile("^[^a-z]*[a-z]+.*$");
    private static Pattern passwordUppercasePattern = Pattern.compile("^[^A-Z]*[A-Z]+.*$");
    private static Pattern passwordNumberPattern = Pattern.compile("^[^0-9]*[0-9]+.*$");

    @Override
    public Object handle(Request request) throws Exception {
        SignupRequest signupRequest = request.getData(SignupRequest.class);

        if(signupRequest == null || signupRequest.getUser() == null || signupRequest.getPassword() == null)
            throw new InvalidRequestBodyExeption("Not all required fields were received.");

        if(!SignupService.usernamePattern.matcher(signupRequest.getUser()).matches() ||
                User.fromUsername(signupRequest.getUser()) != null)
            return new Message("signup-username-unavailable");

        if(!SignupService.passwordLengthPattern.matcher(signupRequest.getPassword()).matches() ||
                !SignupService.passwordLowercasePattern.matcher(signupRequest.getPassword()).matches() ||
                !SignupService.passwordUppercasePattern.matcher(signupRequest.getPassword()).matches() ||
                !SignupService.passwordNumberPattern.matcher(signupRequest.getPassword()).matches())
            return new Message("signup-invalid-password");

        User user = User.create(signupRequest.getUser(), signupRequest.getPassword());

        if(user == null)
            return new Error(500, "Unable to create new user.");

        return new Message("signup-ok");
    }
}
