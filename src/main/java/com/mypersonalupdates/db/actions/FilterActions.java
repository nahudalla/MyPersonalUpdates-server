package com.mypersonalupdates.db.actions;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;


public interface FilterActions {

    // Filter

    @SqlUpdate("INSERT INTO filter (type) VALUES (:type)")
    @GetGeneratedKeys
    Integer create(
            @Bind("type") String type
    );

    @SqlQuery("REMOVE ID FROM filter WHERE ID = :ID LIMIT1")
    Integer remove(
            @Bind("ID") Integer ID
    );


    // NOT Filter

    @SqlUpdate("INSERT INTO not_filter (ID, filterID) VALUES (:ID, :filterID)")
    int createNotFilter(
            @Bind("ID") Integer ID,
            @Bind("filterID") Integer filterID
    );

    @SqlQuery("SELECT ID FROM not_filter WHERE filterID = :filterID LIMIT1")
    Integer notFilterGetIDFromContent(
            @Bind("filterID") Integer filterID
    );

    @SqlQuery("REMOVE ID FROM not_filter WHERE filterID = :filterID LIMIT1")
    Integer notFilterDeleteByID(
            @Bind("filterID") Integer filterID
    );


    // COMPOUND Filter

    @SqlUpdate("INSERT INTO compound_filter (ID, filterID1, filterID2, type) VALUES (:ID, :f1, :f2, :type)")
    int createCompoundFilter(
            @Bind("ID") Integer ID,
            @Bind("filterID1") Integer f1,
            @Bind("filterID2") Integer f2,
            @Bind("type") String type
    );

    @SqlQuery("SELECT ID FROM compound_filter WHERE filterID1 = :f1 AND filterD2 = :f2 LIMIT1")
    Integer compoundFilterGetIDFromContent(
            @Bind("filterID") Integer f1,
            @Bind("filterID") Integer f2
    );

    @SqlQuery("REMOVE ID FROM compound_filter WHERE filterID = :filterID LIMIT1")
    Integer compoundFilterDeleteByID(
            @Bind("filterID") Integer filterID
    );


    // ATTRIBUTE Filter

    @SqlUpdate("INSERT INTO attribute_filter (ID, attrID, fieldValue, type) VALUES (:ID, :attrID, :fieldValue, :type)")
    int createAttributeFilter(
            @Bind("ID") Integer ID,
            @Bind("attrID") Integer attrID,
            @Bind("fieldValue") String fieldValue,
            @Bind("type") String type
    );

    @SqlQuery("REMOVE ID FROM attribute_filter WHERE ID = :ID LIMIT1")
    Integer compoundAttributeDeleteByID(
            @Bind("ID") Integer ID
    );
}
