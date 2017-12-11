package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.mappers.InstantMapper;
import com.mypersonalupdates.db.mappers.ExistsMapper;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * Acciones en la base de datos para la clase {@link Update}
 */
public abstract class UpdateActions {
    @SqlQuery("SELECT ID FROM `update` WHERE ID = :ID")
    @Mapper(ExistsMapper.class)
    public abstract boolean exists(
            @Bind("ID") Long ID
    );

    @SqlUpdate("INSERT INTO `update` (providerID, IDFromProvider, timestamp) VALUES (:providerID, :IDFromProvider, :timestamp)")
    @GetGeneratedKeys
    protected abstract Long insertUpdate(
            @Bind("providerID") Long providerID,
            @Bind("IDFromProvider") String IDFromProvider,
            @Bind("timestamp") Timestamp timestamp
    );

    @SqlBatch("INSERT INTO update_attribute (updateID, providerID, attrID, value) VALUES (:uID, :pID, :aID, :value)")
    protected abstract int[] insertUpdateAttributes(
            @Bind("uID") Long updateID,
            @Bind("pID") Long providerID,
            @Bind("aID") Iterator<Long> attrIDsIterator,
            @Bind("value") Iterator<String> valuesIterator
    );

    @Transaction
    public void create(Update update) throws DBException {
        Long uID = this.insertUpdate(
                update.getProvider().getID(),
                update.getIDFromProvider(),
                new Timestamp(update.getTimestamp().toEpochMilli())
        );

        if(uID == null)
            throw new DBException("Update creation failed: could not insert update into database.");

        Collection<UpdatesProviderAttribute> attributes = update.getProvider().getAttributes();

        if(attributes == null)
            return;

        List<Long> aIDs = new LinkedList<>();
        List<String> values = new LinkedList<>();

        for(UpdatesProviderAttribute attribute : attributes) {
            boolean isMultivalued = attribute.isMultivalued();
            boolean firstInserted = false;

            Collection<String> attrValues = update.getAttributeValues(attribute);

            if(attrValues == null) {
                continue;
            }

            for(String value : attrValues) {
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
            @Bind("providerID") Long providerID,
            @Bind("IDFromProvider") String IDFromProvider
    );

    @SqlQuery("SELECT providerID FROM `update` WHERE ID = :ID")
    public abstract Long getProvider(
            @Bind("ID") Long ID
    );

    @SqlQuery("SELECT timestamp FROM `update` WHERE ID = :ID")
    @RegisterMapper(InstantMapper.class)
    public abstract Instant getTimestamp(
            @Bind("ID") Long ID
    );

    @SqlQuery("SELECT IDFromProvider FROM `update` WHERE ID = :ID")
    public abstract String getIDFromProvider(
            @Bind("ID") Long ID
    );

    @SqlQuery("SELECT value FROM update_attribute WHERE updateID = :updateID AND providerID = :providerID AND attrID = :attrID")
    public abstract List<String> getAttributeValues(
            @Bind("updateID") Long updateID,
            @Bind("providerID") Long providerID,
            @Bind("attrID") Long attrID
    );

}
