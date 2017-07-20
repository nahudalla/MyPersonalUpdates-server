package com.mypersonalupdates.users;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.CategoryActions;
import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.Collection;

public class Category {

    private String name;
    private User user;

    private Category(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public static Category create(User user, String name, Filter filter) throws DBException {

        Integer filterID = Category.getFilterIDFromKeys(user.getID(), name);

        if (filterID == null || !filterID.equals(filter.getID())) {
            int rowsAffected;
            try {
                rowsAffected = DBConnection.getInstance().withHandle(
                        handle -> handle.attach(CategoryActions.class).create(
                                user.getID(),
                                name,
                                filter.getID()
                        )
                );
            } catch (Exception e) {
                throw new DBException(e);
            }

            if (rowsAffected <= 0)
                return null;
        }

        return new Category(user, name);
    }

    public static Category create(User user, String name) throws DBException {
        Integer filterID = Category.getFilterIDFromKeys(user.getID(), name);
        return (filterID == null) ? null : new Category(user, name);
    }

    public boolean remove() throws DBException {
        int rowsAffected;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).remove(
                            this.user.getID(),
                            this.name
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return rowsAffected > 0;
    }

    public User getUser () {
        return this.user;
    }

    public String getName() {
        return  this.name;
    }

    public Collection<UpdatesProvider> getProviders() throws DBException {

        Collection<Integer> providersID;

        try {
            providersID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).getProvidersFromKeys(
                            this.user.getID(),
                            this.name
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        Collection<UpdatesProvider> providers = null;

        if (providersID != null)
            for (Integer ID : providersID)
                providers.add(UpdatesProvidersManager.getProvider(ID));

        return providers;
    }

    public Filter getFilter() throws DBException {
        Integer filterID;

        try {
            filterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).getFilterIDFromKeys(
                            this.user.getID(),
                            name
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return (filterID == null) ? null : Filter.create(filterID);
    }

    public boolean setName(String name) throws DBException {
        int rowsAffected;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).setName(
                            this.user.getID(),
                            this.name,
                            name
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return rowsAffected > 0;
    }

    public boolean setFilter(Filter filter) throws DBException {
        int rowsAffected;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).setFilter(
                            this.user.getID(),
                            this.name,
                            filter.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return rowsAffected > 0;
    }

    public boolean addProvider(UpdatesProvider provider) throws DBException {
        int rowsAffected;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).addProvider(
                            this.user.getID(),
                            this.name,
                            provider.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return rowsAffected > 0;
    }

    public boolean removeProvider(UpdatesProvider provider) throws DBException {
        int rowsAffected;

        try {
            rowsAffected = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).removeProvider(
                            this.user.getID(),
                            this.name,
                            provider.getID()
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return rowsAffected > 0;
    }

    //TODO: Agregar al diagrama de clases
    private static Integer getFilterIDFromKeys(Integer userID, String name) throws DBException {
        Integer FilterID;
        try {
            FilterID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).getFilterIDFromKeys(
                            userID,
                            name
                    )
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        return FilterID;
    }

}
