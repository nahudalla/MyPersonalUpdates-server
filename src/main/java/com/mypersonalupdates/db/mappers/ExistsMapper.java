package com.mypersonalupdates.db.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExistsMapper implements ResultSetMapper<Boolean> {
    @Override
    public Boolean map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return resultSet.first();
    }
}
