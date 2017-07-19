package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface AttributeFilterActions {
    @SqlUpdate("INSERT INTO attribute_filter (ID, attrID, fieldValue, type) VALUES (:ID, :attrID, :fieldValue, :type)")
    int create(
            @Bind("ID") Integer ID,
            @Bind("attrID") Integer attrID,
            @Bind("fieldValue") String fieldValue,
            @Bind("type") String type
    );

    @SqlQuery("SELECT type FROM attribute_filter WHERE filterID = :ID")
    String getTypeFromID(
            @Bind("ID") Integer ID
    );

    @SqlQuery("SELECT ID FROM attribute_filter WHERE attrID = :attrID AND fieldValue = :value AND type = :type LIMIT 1")
    Integer getIDFromContent(
            @Bind("attrID") Integer attrID,
            @Bind("value") String value,
            @Bind("value") String type
    );

    @SqlQuery("SELECT attrID FROM attribute_filter WHERE ID = :ID AND type = :type")
    Integer getAttrIDFromKeys(
      @Bind("ID") Integer ID,
      @Bind("value") String type
    );

    @SqlQuery("SELECT fieldValue FROM attribute_filter WHERE ID = :ID AND type = :type")
    String getFieldValueDFromKeys(
      @Bind("ID") Integer ID,
      @Bind("value") String type
    );
}
