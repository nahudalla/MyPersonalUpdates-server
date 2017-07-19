package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface AttributeFilterActions {
    @SqlUpdate("INSERT INTO attribute_filter (ID, attrID, fieldValue, TYPE) VALUES (:ID, :attrID, :fieldValue, :TYPE)")
    int create(
            @Bind("ID") Integer ID,
            @Bind("attrID") Integer attrID,
            @Bind("fieldValue") String fieldValue,
            @Bind("TYPE") String type
    );

    @SqlQuery("SELECT type FROM attribute_filter WHERE ID = :ID")
    String getTypeFromID(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT ID FROM attribute_filter WHERE attrID = :attrID AND fieldValue = :value AND TYPE = :TYPE LIMIT 1")
    Integer getIDFromContent(
            @Bind("attrID") Integer attrID,
            @Bind("value") String value,
            @Bind("TYPE") String type
    );

    @SqlQuery("SELECT attrID FROM attribute_filter WHERE ID = :ID")
    Integer getAttrIDFromID(
      @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT fieldValue FROM attribute_filter WHERE ID = :ID")
    String getFieldValueFromID(
      @Bind("ID") Integer ID
    );
}
