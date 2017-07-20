package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.db.mappers.UpdatesProviderAttributeMapper;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface AttributeFilterActions {
    @SqlUpdate("INSERT INTO attribute_filter (ID, providerID, attrID, fieldValue, TYPE) VALUES (:ID, :providerID, :attrID, :fieldValue, :TYPE)")
    int create(
            @Bind("ID") Integer ID,
            @Bind("providerID") Integer providerID,
            @Bind("attrID") Integer attrID,
            @Bind("fieldValue") String fieldValue,
            @Bind("TYPE") String type
    );

    @SqlQuery("SELECT type FROM attribute_filter WHERE ID = :ID")
    String getTypeFromID(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT ID FROM attribute_filter WHERE providerID = :providerID AND attrID = :attrID AND fieldValue = :value AND TYPE = :TYPE LIMIT 1")
    Integer getIDFromContent(
            @Bind("providerID") Integer providerID,
            @Bind("attrID") Integer attrID,
            @Bind("value") String value,
            @Bind("TYPE") String type
    );

    @SqlQuery("SELECT providerID, attrID FROM attribute_filter WHERE ID = :ID")
    @RegisterMapper(UpdatesProviderAttributeMapper.class)
    UpdatesProviderAttribute getAttrFromID(
      @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT fieldValue FROM attribute_filter WHERE ID = :ID")
    String getFieldValueFromID(
      @Bind("ID") Integer ID
    );
}
