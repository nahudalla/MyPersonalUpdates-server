package com.mypersonalupdates.log;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UpdateActions;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.time.Instant;
import java.util.Collection;
import java.util.Queue;

public final class LogUpdate implements Update{
    private final Long ID;

    private LogUpdate(Long ID){
        this.ID = ID;
    }

    public static LogUpdate create(Long ID) throws DBException {
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

    public static void create(Queue<Update> updates) throws DBException {
        try {
            DBConnection.getInstance().withHandle((handle)-> {
                UpdateActions actions = handle.attach(UpdateActions.class);

                Update update;
                while(!updates.isEmpty()) {
                    update = updates.remove();
                    if(!actions.existsProviderData(
                            update.getProvider().getID(),
                            update.getIDFromProvider()
                    ))
                        actions.create(update);
                }

                return null;
            });
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    public UpdatesProvider getProvider() {
        try {
            Long providerID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getProvider(
                            this.ID
                    )
            );

            return (providerID == null) ? null : UpdatesProvidersManager.getInstance().getProvider(providerID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Instant getTimestamp() {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getTimestamp(
                            this.ID
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection<String> getAttributeValues(UpdatesProviderAttribute attr) {
        try {
            return  DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getAttributeValues(
                            this.ID,
                            attr.getProvider().getID(),
                            attr.getAttrID()
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getIDFromProvider() {
        try {
            return DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getIDFromProvider(
                            this.ID
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogUpdate)) return false;

        LogUpdate logUpdate = (LogUpdate) o;

        return ID.equals(logUpdate.ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
