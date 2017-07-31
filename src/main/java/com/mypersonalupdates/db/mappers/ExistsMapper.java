package com.mypersonalupdates.db.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase toma el resultado de una consulta en la base de datos y
 * devuelve `true` si dicha consulta devolvió algún registro.
 */
public final class ExistsMapper implements ResultSetMapper<Boolean> {
    @Override
    public Boolean map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return resultSet.first();
    }
}
