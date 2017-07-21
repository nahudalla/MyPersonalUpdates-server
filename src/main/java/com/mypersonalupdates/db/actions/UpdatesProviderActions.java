package com.mypersonalupdates.db.actions;

import com.mypersonalupdates.db.mappers.UpdatesProviderAttributeMapper;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

public interface UpdatesProviderActions {
    @SqlQuery("SELECT name FROM updates_provider WHERE ID = :id")
    String getName(
            @Bind("id") Integer id
    );

    @SqlQuery("SELECT description FROM updates_provider WHERE ID = :id")
    String getDescription(
            @Bind("id") Integer id
    );

    @SqlQuery("SELECT providerID, attrID FROM updates_provider_attribute_association WHERE providerID = :id")
    @Mapper(UpdatesProviderAttributeMapper.class)
    List<UpdatesProviderAttribute> getAttributes(
            @Bind("id") Integer id
    );
}
