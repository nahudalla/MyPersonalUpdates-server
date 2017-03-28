package com.mypersonalupdates.users;

import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UserActions;

import java.security.MessageDigest;

public class User {
    private Integer id = null;

    private static String sha256(String value) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            byte[] bytes = md.digest();
            StringBuffer result = new StringBuffer();
            for (byte b : bytes)
                result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            return result.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static User create(String username, String password) throws DBException {
        int rowsAffected;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(handle -> {
                UserActions userActions = handle.attach(UserActions.class);
                return userActions.createUser(
                        username,
                        User.sha256(password)
                );
            });
        }catch (Exception e) {
            throw new DBException(e);
        }

        if(rowsAffected == 0)
            return null;
        else
            return User.fromUsername(username);
    }

    public static User fromCredentials(String username, String password) throws DBException {
        Integer UID;

        try {
            UID = DBConnection.getInstance().withHandle(handle -> {
                UserActions userActions = handle.attach(UserActions.class);
                return userActions.validateLoginData(
                        username,
                        User.sha256(password)
                );
            });
        }catch (Exception e) {
            throw new DBException(e);
        }

        if(UID == null)
            return null;
        else
            return new User(UID);
    }

    public static User fromId(Integer id) throws DBException {
        Integer UID;

        try {
            UID = DBConnection.getInstance().withHandle(handle -> {
                UserActions userActions = handle.attach(UserActions.class);
                return userActions.validateId(id);
            });
        }catch (Exception e) {
            throw new DBException(e);
        }

        if(UID == null)
            return null;
        else
            return new User(UID);
    }

    public static User fromUsername(String username) throws DBException {
        Integer UID;

        try {
            UID = DBConnection.getInstance().withHandle(handle -> {
                UserActions userActions = handle.attach(UserActions.class);
                return userActions.idFromUsername(username);
            });
        }catch (Exception e) {
            throw new DBException(e);
        }

        if(UID == null)
            return null;
        else
            return new User(UID);
    }

    private User() {}

    private User(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public String getUsername() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(handle -> {
                UserActions userActions = handle.attach(UserActions.class);
                return userActions.usernameFromId(this.id);
            });
        }catch (Exception e) {
            throw new DBException(e);
        }
    }
}
