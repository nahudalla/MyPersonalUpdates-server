package com.mypersonalupdates.users;

import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.CategoryActions;
import com.mypersonalupdates.log.Log;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.realtime.RealTimeStreamsManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Esta clase representa una categoría de actualizaciones
 * asociada a un usuario específico. Las actualizaciones
 * que pertenecen a la categoría están determinadas por
 * un filtro.
 * */
public final class Category {

    private String name;
    private final User user;

    private Category(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public static Category create(User user, String name, Filter filter) throws DBException, UserNotLoggedInToProviderException {
        // Chequeo si ya existe una categoría asociada a user con nombre name, y sino la creo
        Long filterID = Category.getFilterIDFromKeys(user.getID(), name);
        if (filterID == null) {
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

            Category newCategory = new Category(user, name);


            try {
                Log.getInstance().subscribeToCategoryUpdates(newCategory);
            } catch (UserNotLoggedInToProviderException e) {

                //noinspection finally
                try {
                    newCategory.remove();
                } catch (Exception e2) {
                    // ignore
                    e.printStackTrace();
                } finally {
                    throw e;
                }
            }



            return newCategory;
        }

        // Si ya existía y el filtro es el mismo, retorno la categoría ya existente
        return filterID.equals(filter.getID()) ? new Category(user, name) : null;
    }

    public static Category create(User user, String name) throws DBException {
        Long filterID = Category.getFilterIDFromKeys(user.getID(), name);
        return (filterID == null) ? null : new Category(user, name);
    }

    public void suspendRealTimeSubscriptions() {
        RealTimeStreamsManager.getInstance().suspendCategoryUpdates(this);
    }

    public void resumeRealTimeSubscriptions() throws DBException, UserNotLoggedInToProviderException {
        RealTimeStreamsManager.getInstance().resumeCategoryUpdates(this);
    }

    public boolean remove() throws DBException {
        int rowsAffected;

        Filter oldFilter = this.getFilter();

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

        if(rowsAffected > 0) {
            RealTimeStreamsManager.getInstance().categoryDeleted(this);
            oldFilter.remove();
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

        Iterator<Long> providersID;

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

        Collection<UpdatesProvider> providers = new LinkedList<>();

        if (providersID != null)
            while(providersID.hasNext()) {
                UpdatesProvider provider = UpdatesProvidersManager.getInstance().getProvider(providersID.next());
                if(provider != null)
                    providers.add(provider);
            }

        return providers;
    }

    public Filter getFilter() throws DBException {
        Long filterID = getFilterIDFromKeys(this.user.getID(), this.name);
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

        if(rowsAffected > 0)
            this.name = name;

        return rowsAffected > 0;
    }

    public boolean setFilter(Filter filter) throws DBException, UserNotLoggedInToProviderException {
        int rowsAffected;

        Filter oldFilter = this.getFilter();

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

        if(rowsAffected > 0) {
            oldFilter.remove();
            RealTimeStreamsManager.getInstance().categoryUpdated(this);
        }

        return rowsAffected > 0;
    }

    public boolean addProvider(UpdatesProvider provider) throws DBException, UserNotLoggedInToProviderException {
        try {
            boolean isAssociated = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(CategoryActions.class).isProviderAssociated(
                            this.user.getID(),
                            this.name,
                            provider.getID()
                    )
            );

            if(isAssociated)
                return true;
        }catch (Exception e) {
            throw new DBException(e);
        }

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

        if(rowsAffected > 0)
            RealTimeStreamsManager.getInstance().categoryUpdated(this);

        return rowsAffected > 0;
    }

    public boolean removeProvider(UpdatesProvider provider) throws DBException, UserNotLoggedInToProviderException {
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

        if(rowsAffected > 0)
            RealTimeStreamsManager.getInstance().categoryUpdated(this);

        return rowsAffected > 0;
    }

    private static Long getFilterIDFromKeys(Long userID, String name) throws DBException {
        Long FilterID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        return getName().equals(category.getName()) && getUser().equals(category.getUser());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getUser().hashCode();
        return result;
    }
}
