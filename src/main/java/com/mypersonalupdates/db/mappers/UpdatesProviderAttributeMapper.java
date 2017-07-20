package com.mypersonalupdates.db.mappers;

import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdatesProviderAttributeMapper implements ResultSetMapper<UpdatesProviderAttribute> {
    @Override
    public UpdatesProviderAttribute map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Integer providerID = resultSet.findColumn("providerID");
        Integer attrID = resultSet.findColumn("attrID");
        return UpdatesProviderAttribute.create(
                UpdatesProvidersManager.getInstance().getProvider(providerID),
                attrID
        );
    }
}
