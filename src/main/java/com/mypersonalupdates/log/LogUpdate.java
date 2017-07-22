package com.mypersonalupdates.log;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UpdateActions;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.util.Collection;
import java.util.Date;

public class LogUpdate implements Update{
    private Integer ID;

    private LogUpdate(Integer ID){
        this.ID = ID;
    }

    public static LogUpdate create(Integer ID) throws DBException {
        try {
            boolean exists = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).exists(
                            ID)
            );

            return !exists ? null : new LogUpdate(ID);

        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public static LogUpdate create(Update update) throws DBException {
        Integer providerID = update.getProvider().getID();
        String IDFRomProvider = update.getIDFromProvider();

        Integer ID = LogUpdate.getIDFromProviderData(providerID, IDFRomProvider);

        if (ID == null) {

            try {
                ID = DBConnection.getInstance().withHandle(
                        handle -> handle.attach(UpdateActions.class).create(
                                providerID,
                                IDFRomProvider,
                                update.getTimestamp()
                        )
                );
            } catch (Exception e) {
                throw new DBException(e);
            }
        }

        return (ID == null) ? null : new LogUpdate(ID);
    }

    private static Integer getIDFromProviderData(Integer providerID, String IDFromProvider) throws DBException {
        Integer ID;

        try {
            ID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getIDFromProviderData(
                            providerID,
                            IDFromProvider
                    )
            );

            return ID;

        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public UpdatesProvider getProvider() throws DBException {
        try {
            Integer providerID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getProvider(
                            this.ID
                    )
            );

            return (providerID == null) ? null : UpdatesProvidersManager.getInstance().getProvider(providerID);

        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public Date getTimestamp() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getTimestamp(
                            this.ID
                    )
            );

        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public Collection<String> getAttributeValues(UpdatesProviderAttribute attr) throws DBException {
        try {
            return  DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getAttributeValues(
                            this.ID,
                            attr.getProvider().getID(),
                            attr.getAttrID()
                    )
            );

        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    @Override
    public String getIDFromProvider() throws DBException {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getIDFromProvider(
                            this.ID
                    )
            );

        } catch (Exception e) {
            throw new DBException(e);
        }
    }
}
