package com.mypersonalupdates.log;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.UpdateActions;
import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.Collection;
import java.util.Date;

public class LogUpdate{
    Integer ID;

    private LogUpdate(Integer ID){
        this.ID = ID;
    }

    public static LogUpdate create(Update update){

    }

    public static LogUpdate create(Integer ID) throws DBException {
        Integer auxID = null;
        try {
            auxID = DBConnection.getInstance().withHandle(
                    handle -> handle.attach(UpdateActions.class).getIDFromID(
                            ID)
            );
        } catch (Exception e) {
            throw new DBException(e);
        }

        if (auxID.equals(ID))
            return new LogUpdate(ID);

        return null;
    }

    public UpdatesProvider getProvider(){

    }

    public Date getTimestamp(){

    }

    public Collection<String> getAttributeValues(UpdatesProvider attr){

    }
}
