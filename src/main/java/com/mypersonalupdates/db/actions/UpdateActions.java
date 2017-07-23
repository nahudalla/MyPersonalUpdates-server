package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.mappers.ExistsMapper;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.*;

public abstract class UpdateActions {

    @SqlQuery("SELECT ID FROM `update` WHERE ID = :ID")
    @Mapper(ExistsMapper.class)
    public abstract boolean exists(
            @Bind("ID") Integer ID
    );

    @SqlUpdate("INSERT INTO `update` (providerID, IDFromProvider, timestamp) VALUES (:providerID, :IDFromProvider, :timestamp)")
    @GetGeneratedKeys
    protected abstract Integer insertUpdate(
            @Bind("providerID") Integer providerID,
            @Bind("IDFromProvider") String IDFromProvider,
            @Bind("timestamp") Date timestamp
    );

    @SqlBatch("INSERT INTO update_attribute (updateID, providerID, attrID, value) VALUES (:uID, :pID, :aID, :value)")
    protected abstract int[] insertUpdateAttributes(
            @Bind("uID") Integer updateID,
            @Bind("pID") Integer providerID,
            @Bind("aID") Iterator<Integer> attrIDsIterator,
            @Bind("value") Iterator<String> valuesIterator
    );

    @Transaction
    public void create(Update update) throws DBException {
        Integer uID = this.insertUpdate(
                update.getProvider().getID(),
                update.getIDFromProvider(),
                update.getTimestamp()
        );

        if(uID == null)
            throw new DBException("Update creation failed: could not insert update into database.");

        Collection<UpdatesProviderAttribute> attributes = update.getProvider().getAttributes();

        if(attributes == null)
            return;

        List<Integer> aIDs = new LinkedList<>();
        List<String> values = new LinkedList<>();

        for(UpdatesProviderAttribute attribute : attributes) {
            boolean isMultivalued = attribute.isMultivalued();
            boolean firstInserted = false;

            for(String value : update.getAttributeValues(attribute)) {
                if(isMultivalued && firstInserted)
                    break;

                if(value != null && value.length() > 0) {
                    firstInserted = true;
                    aIDs.add(attribute.getAttrID());
                    values.add(value);
                }
            }
        }

        int[] rowsAffected = this.insertUpdateAttributes(
                uID,
                update.getProvider().getID(),
                aIDs.iterator(),
                values.iterator()
        );

        for(int rA : rowsAffected) {
            if (rA < 1) {
                throw new DBException("Update creation failed: could not insert update attribute into database.");
            }
        }
    }

    @SqlQuery("SELECT IDFromProvider FROM `update` WHERE providerID = :providerID AND IDFromProvider = :IDFromProvider")
    @Mapper(ExistsMapper.class)
    public abstract boolean existsProviderData(
            @Bind("providerID") Integer providerID,
            @Bind("IDFromProvider") String IDFromProvider
    );

    @SqlQuery("SELECT providerID FROM `update` WHERE ID = :ID")
    public abstract Integer getProvider(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT timestamp FROM `update` WHERE ID = :ID")
    public abstract Date getTimestamp(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT IDFromProvider FROM `update` WHERE ID = :ID")
    public abstract String getIDFromProvider(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT value FROM update_attribute WHERE updateID = :updateID AND providerID = :providerID AND attrID = :attrID")
    public abstract List<String> getAttributeValues(
            @Bind("updateID") Integer updateID,
            @Bind("providerID") Integer providerID,
            @Bind("attrID") Integer attrID
    );

}
