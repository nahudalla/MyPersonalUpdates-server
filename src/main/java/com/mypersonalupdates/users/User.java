package com.mypersonalupdates.users;

import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UserActions;
import com.mypersonalupdates.providers.UpdatesProvider;

import javax.jws.soap.SOAPBinding;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class User {
    // TODO: soporte para cambio de contraseñas

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

    public static User createNew(String username, String password) throws DBException {
        int rowsAffected;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).createUser(
                            username,
                            User.sha256(password)
                    )
            );
        }catch (Exception e) {
            throw new DBException(e);
        }

        if(rowsAffected == 0)
            return null;
        else
            return User.getFromUsername(username);
    }

    public static User getFromCredentials(String username, String password) throws DBException {
        Integer UID;

        try {
            UID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).validateLoginData(
                            username,
                            User.sha256(password)
                    )
            );
        }catch (Exception e) {
            throw new DBException(e);
        }

        if(UID == null)
            return null;
        else
            return new User(UID);
    }

    public static User getFromID(Integer id) throws DBException {
        Integer UID;

        try {
            UID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).validateId(id)
            );
        }catch (Exception e) {
            throw new DBException(e);
        }

        if(UID == null)
            return null;
        else
            return new User(UID);
    }

    public static User getFromUsername(String username) throws DBException {
        Integer UID;

        try {
            UID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).idFromUsername(username)
            );
        }catch (Exception e) {
            throw new DBException(e);
        }

        if(UID == null)
            return null;
        else
            return new User(UID);
    }

    private User(Integer id){
        this.id = id;
    }

    public Integer getID() {
        return this.id;
    }

    public String getUsername() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).usernameFromId(this.id)
            );
        }catch (Exception e) {
            throw new DBException(e);
        }
    }

    public Collection<Category> getCategories() throws DBException {
        Iterator<String> iterator;

        try {
            iterator = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).categoryNamesFromUserID(this.getID())
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if(iterator == null || !iterator.hasNext())
            return null;

        Collection<Category> categories = new LinkedList<>();

        while(iterator.hasNext())
            categories.add(Category.create(this, iterator.next()));

        return categories;
    }

    public String getPassword() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).getPasswordFromID(this.getID())
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public boolean remove() throws DBException {
        try {
            return 1 == DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).removeUserFromID(this.getID())
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    // TODO: arreglar nombre name->attributeName en diagrama
    public String getAttribute(UpdatesProvider provider, String attributeName) throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).getProviderAttribute(
                            this.getID(),
                            provider.getID(),
                            attributeName
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    // TODO: arreglar nombres en diagrama
    void setAttribute(UpdatesProvider provider, String attributeName, String newValue) throws DBException {
        try {
            DBConnection.getInstance().withHandle(
                    (DBConnection.HandleCallback<Void>) handle -> {
                        handle.attach(UserActions.class).setProviderAttribute(
                                this.getID(),
                                provider.getID(),
                                attributeName,
                                newValue
                        );
                        return null;
                    });
        } catch (Exception e) {
            throw new DBException(e);
        }
    }
}
