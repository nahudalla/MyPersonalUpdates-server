package com.mypersonalupdates.users;

import com.google.common.hash.Hashing;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UserActions;
import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Esta clase representa a un usuario del sistema
 * y administra su información asociada, como sus
 * categorías y los datos almacenados por los
 * proveedores que utiliza.
 */
public final class User {
    // TODO: soporte para cambio de contraseñas

    private Long id = null;

    private static String sha256(String value) {
        return Hashing.sha256().hashString(value).toString();
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
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (rowsAffected == 0)
            return null;
        else
            return User.getFromUsername(username);
    }

    public static User getFromCredentials(String username, String password) throws DBException {
        Long UID;

        try {
            UID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).validateLoginData(
                            username,
                            User.sha256(password)
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (UID == null)
            return null;
        else
            return new User(UID);
    }

    public static User getFromID(Long id) throws DBException {
        Long UID;

        try {
            UID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).validateId(id)
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (UID == null)
            return null;
        else
            return new User(UID);
    }

    public static User getFromUsername(String username) throws DBException {
        Long UID;

        try {
            UID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).idFromUsername(username)
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (UID == null)
            return null;
        else
            return new User(UID);
    }

    public static User getInstanceFromExistingID(Long ID) {
        return new User(ID);
    }

    private User(Long id) {
        this.id = id;
    }

    public Long getID() {
        return this.id;
    }

    public String getUsername() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UserActions.class).usernameFromId(this.id)
            );
        } catch (Exception e) {
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

        if (iterator == null || !iterator.hasNext())
            return null;

        Collection<Category> categories = new LinkedList<>();

        while (iterator.hasNext())
            categories.add(Category.create(this, iterator.next()));

        return categories;
    }

    public boolean remove() throws DBException {
        try {
            for (Category category : this.getCategories())
                category.remove();

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
    public void setAttribute(UpdatesProvider provider, String attributeName, String newValue) throws DBException {
        UserActions actions = DBConnection.getInstance().onDemand(UserActions.class);

        try {
            if (actions.existsProviderAttribute(this.getID(), provider.getID(), attributeName)) {
                actions.setProviderAttribute(
                        this.getID(),
                        provider.getID(),
                        attributeName,
                        newValue
                );
            } else {
                actions.insertProviderAttribute(
                        this.getID(),
                        provider.getID(),
                        attributeName,
                        newValue
                );
            }

        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
